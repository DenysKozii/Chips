package degradation.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PagesUtility {
	private final static int DEFAULT_PAGE = 0;
	private final static int DEFAULT_SIZE = 10;

	public static Pageable createPageableUnsorted(Integer page, Integer pageSize) {
		return PageRequest.of(Objects.nonNull(page) ? page : DEFAULT_PAGE, Objects.nonNull(pageSize) ? pageSize : DEFAULT_SIZE);
	}

	public static Pageable createSortPageRequest(Integer page, Integer pageSize, boolean asc, String... fields) {
		return PageRequest.of(Objects.nonNull(page) ? page : DEFAULT_PAGE, Objects.nonNull(pageSize) ? pageSize : DEFAULT_SIZE, asc ? Sort.by(fields).ascending() : Sort.by(fields).descending());
	}

	public static Pageable createSortPageRequest(Integer page, Integer pageSize, String... fields) {
		return createSortPageRequest(page, pageSize, false, fields);
	}

	public static Pageable createPageableSortAsc(Integer page, Integer pageSize, String... fields) {
		return PageRequest.of(Objects.nonNull(page) ? page : DEFAULT_PAGE, Objects.nonNull(pageSize) ? pageSize : DEFAULT_SIZE, Sort.by(fields).ascending());
	}

	public static Pageable createPageableSortDesc(Integer page, Integer pageSize, String... fields) {
		return PageRequest.of(Objects.nonNull(page) ? page : DEFAULT_PAGE, Objects.nonNull(pageSize) ? pageSize : DEFAULT_SIZE, Sort.by(fields).descending());
	}

	public int getDefaultPage() {
		return DEFAULT_PAGE;
	}

	public int getDefaultSize() {
		return DEFAULT_SIZE;
	}
}
