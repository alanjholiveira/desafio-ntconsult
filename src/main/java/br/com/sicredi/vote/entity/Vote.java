package br.com.sicredi.vote.entity;

import br.com.sicredi.vote.enums.VoteType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_vote")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private UUID id;

    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pauta pauta;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @CreatedDate
    private LocalDateTime createdAt;

}