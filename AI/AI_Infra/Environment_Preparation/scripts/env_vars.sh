export VLLM_USE_MODELSCOPE=true
export VLLM_USE_V1=1
export MODELSCOPE_CACHE=/home/cache/modelscope/hub/

export PATH=/usr/local/python3.11.13/bin:${PATH}

# Ascend Toolkit
export ASCEND_TOOLKIT_HOME=/usr/local/Ascend/ascend-toolkit/latest
export LD_LIBRARY_PATH=/usr/local/Ascend/driver/lib64/common/:/usr/local/Ascend/driver/lib64/driver/:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH=${ASCEND_TOOLKIT_HOME}/lib64:${ASCEND_TOOLKIT_HOME}/lib64/plugin/opskernel:${ASCEND_TOOLKIT_HOME}/lib64/plugin/nnengine:${ASCEND_TOOLKIT_HOME}/opp/built-in/op_impl/ai_core/tbe/op_tiling:$LD_LIBRARY_PATH
export LD_LIBRARY_PATH=${ASCEND_TOOLKIT_HOME}/tools/aml/lib64:${ASCEND_TOOLKIT_HOME}/tools/aml/lib64/plugin:$LD_LIBRARY_PATH
export PYTHONPATH=${ASCEND_TOOLKIT_HOME}/python/site-packages:${ASCEND_TOOLKIT_HOME}/opp/built-in/op_impl/ai_core/tbe:$PYTHONPATH
export PATH=${ASCEND_TOOLKIT_HOME}/bin:${ASCEND_TOOLKIT_HOME}/compiler/ccec_compiler/bin:${ASCEND_TOOLKIT_HOME}/tools/ccec_compiler/bin:$PATH
export ASCEND_AICPU_PATH=${ASCEND_TOOLKIT_HOME}
export ASCEND_OPP_PATH=${ASCEND_TOOLKIT_HOME}/opp
export TOOLCHAIN_HOME=${ASCEND_TOOLKIT_HOME}/toolkit
export ASCEND_HOME_PATH=${ASCEND_TOOLKIT_HOME}

# NNAL
export ATB_HOME_PATH=/usr/local/Ascend/nnal/atb/latest/atb/cxx_abi_0
export LD_LIBRARY_PATH=${ATB_HOME_PATH}/lib:${ATB_HOME_PATH}/examples:${ATB_HOME_PATH}/tests/atbopstest:${LD_LIBRARY_PATH}
export PATH=${ATB_HOME_PATH}/bin:$PATH
export ATB_STREAM_SYNC_EVERY_KERNEL_ENABLE=0
export ATB_STREAM_SYNC_EVERY_RUNNER_ENABLE=0
export ATB_STREAM_SYNC_EVERY_OPERATION_ENABLE=0
export ATB_OPSRUNNER_SETUP_CACHE_ENABLE=1
export ATB_OPSRUNNER_KERNEL_CACHE_LOCAL_COUNT=1
export ATB_OPSRUNNER_KERNEL_CACHE_GLOABL_COUNT=5
export ATB_WORKSPACE_MEM_ALLOC_ALG_TYPE=1
export ATB_WORKSPACE_MEM_ALLOC_GLOBAL=0
export ATB_COMPARE_TILING_EVERY_KERNEL=0
export ATB_HOST_TILING_BUFFER_BLOCK_NUM=128
export ATB_DEVICE_TILING_BUFFER_BLOCK_NUM=32
export ATB_SHARE_MEMORY_NAME_SUFFIX=""
export ATB_MATMUL_SHUFFLE_K_ENABLE=1
export ASDOPS_LOG_LEVEL=ERROR
export ASDOPS_LOG_TO_STDOUT=0
export ASDOPS_LOG_TO_FILE=1
export ASDOPS_LOG_TO_FILE_FLUSH=0
export ASDOPS_LOG_TO_BOOST_TYPE=atb
export ASDOPS_LOG_PATH=/root
export LCCL_DETERMINISTIC=0
export LCCL_PARALLEL=0

source /usr/local/Ascend/ascend-toolkit/set_env.sh
source /usr/local/Ascend/nnal/atb/set_env.sh

# 使用 ASCEND_VISIBLE_DEVICES 环境变量指定被挂载至容器中的 NPU 设备
