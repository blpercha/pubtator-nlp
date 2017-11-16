package pubtator;

import java.util.Objects;

public class IndexedToken {
	private final String token;
	private final int startPosition;
	private final int endPosition;

	public IndexedToken(String token, int startPosition, int endPosition) {
		this.token = token;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

	public String word() {
		return token;
	}

	public int startPosition() {
		return startPosition;
	}

	public int endPosition() {
		return endPosition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IndexedToken)) return false;
		IndexedToken that = (IndexedToken) o;
		return startPosition() == that.startPosition() &&
				endPosition() == that.endPosition() &&
				Objects.equals(word(), that.word());
	}

	@Override
	public int hashCode() {
		return Objects.hash(word(), startPosition(), endPosition());
	}

	@Override
	public String toString() {
		return "IndexedToken{" +
				"token='" + token + '\'' +
				", startPosition=" + startPosition +
				", endPosition=" + endPosition +
				'}';
	}
}
