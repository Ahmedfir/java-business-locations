import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public class DummyClassWithStaticInvocation {

    public static @NotNull Stream<String> streamThroughCommentsIfApplicable(@Nullable Object object,
                                                                            @Nullable Set<UUID> usedCommentIds) {
        return Stream.empty();
    }

    public static @NotNull Stream<String> streamThroughCommentsIfApplicableQualifiedName(@Nullable Object object,
                                                                            @Nullable Set<UUID> usedCommentIds) {
        return java.util.stream.Stream.empty();
    }

    public static @NotNull Stream<String> streamThroughCommentsIfApplicableMethodArguments(@Nullable Object object,
                                                                                         @Nullable Set<UUID> usedCommentIds) {
        return java.util.stream.Stream.equals(45, "forty-five");
    }

    public static @NotNull Stream<String> streamThroughCommentsIfApplicableObject(@Nullable Object object,
    @Nullable Set<UUID> usedCommentIds) {
        Stream st = new Stream()
            return st.empty();
    }
}
