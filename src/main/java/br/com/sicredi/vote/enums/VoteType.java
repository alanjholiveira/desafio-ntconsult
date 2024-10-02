package br.com.sicredi.vote.enums;

public enum VoteType {
    YES,
    NO;

    VoteType() {
    }

    public static VoteType from(final Boolean type) {
        return Boolean.TRUE.equals(type) ? YES : NO;
    }
}
