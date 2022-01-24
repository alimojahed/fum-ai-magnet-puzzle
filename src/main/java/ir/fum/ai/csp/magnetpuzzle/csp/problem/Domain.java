package ir.fum.ai.csp.magnetpuzzle.csp.problem;

import com.google.common.collect.Sets;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ali Mojahed on 12/24/2021
 * @project magnet-puzzle
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Domain<T> {
    private Set<T> legalValues = new HashSet<>();

    public static <T extends Enum<T>> Domain<T> domainFromEnum(Class<T> clazz, Iterable<T> iterable) {
        return new Domain<>(Sets.newEnumSet(iterable, clazz));
    }

    public void removeValue(T value) {
        legalValues.remove(value);
    }

    public void addValue(T value) {
        legalValues.add(value);
    }

}
