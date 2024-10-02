package br.com.sicredi.vote.event.out;

import java.util.Map;

public record ResultPollVotesDetailOutput(
        String poll,
        Integer countVotes,
        Map<String, Integer> questions
) {

    public static ResultPollVotesDetailOutput from(final String poll, final Integer countVotes,
                                                   final Map<String, Integer> questions) {
        return new ResultPollVotesDetailOutput(poll, countVotes, questions);
    }

}
