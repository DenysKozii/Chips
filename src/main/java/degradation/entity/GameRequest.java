package degradation.entity;

import degradation.enums.RequestStatus;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "game_requests")
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest extends BaseEntity {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invitor_id")
    private User invitor;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acceptor_id")
    private User acceptor;

    @NonNull
    private RequestStatus status;
}
