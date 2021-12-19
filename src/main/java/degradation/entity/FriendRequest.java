package degradation.entity;

import degradation.enums.RequestStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend_requests")
public class FriendRequest extends BaseEntity{

    @NonNull
    private String invitorUsername;

    @NonNull
    private String acceptorUsername;

    @NonNull
    private RequestStatus status;

}