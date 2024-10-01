package br.com.sicredi.vote.entity;

import br.com.sicredi.vote.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_session")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Pauta pauta;

    @Column(nullable = false)
    private LocalDateTime expiration;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @OneToMany(mappedBy = "session", fetch = FetchType.EAGER)
    private List<Vote> votes;

    public Boolean isOpenSession() {
        if (!ObjectUtils.isEmpty(this.expiration)) {
            final LocalDateTime localDateTimeNow = LocalDateTime.now();
            return this.expiration.isAfter(localDateTimeNow);
        }

        return false;
    }

}
