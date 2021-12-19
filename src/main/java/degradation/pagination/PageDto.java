package degradation.pagination;

import lombok.Data;

import java.util.List;

@Data(staticConstructor = "of")
public class PageDto<T> {
    private final int total;
    private final int page;
    private final List<T> objects;

    public static <T> PageDto<T> of(long total, int page, List<T> objects) {
        return new PageDto<>((int) total, page, objects);
    }
}
