from dataclasses import dataclass


@dataclass
class SpeculativeProposals:
    """Datastructure used to represent proposal tokens from some proposer. It
    also tracks how many speculative tokens each sequence has.
    """

    # Speculative proposal tokens.
    proposal_token_ids: int
    # Probabilities of the proposal tokens according to the proposer.
    proposal_probs: int

    # The valid length of each proposal; can be zero.
    proposal_lens: int

    # A flag to mark that there's no available proposals
    no_proposals: bool = False

    def __repr__(self):
        return (f"SpeculativeProposals("
                f"proposal_token_ids={self.proposal_token_ids}, "
                f"proposal_probs={self.proposal_probs}, "
                f"proposal_lens={self.proposal_lens})")


if __name__ == '__main__':
    sp = SpeculativeProposals(1, 2, 3)
    print(sp)
