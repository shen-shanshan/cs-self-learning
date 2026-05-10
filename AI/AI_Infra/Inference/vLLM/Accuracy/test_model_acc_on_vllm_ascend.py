import lm_eval
import pytest

MODEL_NAMES = ["Qwen/Qwen3-30B-A3B"]
NUM_CONCURRENT = 500
TASK = "gsm8k"
FILTER = "exact_match,strict-match"
RTOL = 0.03
EXPECTED_VALUES = {"Qwen/Qwen3-30B-A3B": 0.5}


def run_test(model_name, more_args=None):
    """Run the end to end accuracy test."""

    # NOTE: Do not add any spaces to the string below, as this will cause parameter parsing errors.
    model_args = f"pretrained={model_name},enforce_eager=True,tensor_parallel_size=2,enable_expert_parallel=True"

    if more_args is not None:
        model_args = "{},{}".format(model_args, more_args)

    results = lm_eval.simple_evaluate(
        model="vllm",
        model_args=model_args,
        tasks="gsm8k",
        batch_size="auto",
    )

    measured_value = results["results"][TASK][FILTER]
    assert model_name in EXPECTED_VALUES, f"Cannot find the expected value for the model {model_name=}"
    expected_value = EXPECTED_VALUES[model_name]
    assert measured_value - RTOL < expected_value and measured_value + RTOL > expected_value, (
        f"Expected: {expected_value} |  Measured: {measured_value}"
    )


@pytest.mark.parametrize("model", MODEL_NAMES)
def test_lm_eval_accuracy(model):
    """Run with the V1 Engine."""
    more_args = None
    run_test(model, more_args)

"""
Main: 0.8968915845337376
0427: 0.4397270659590599
"""
