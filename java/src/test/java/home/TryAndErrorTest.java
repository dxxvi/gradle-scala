package home;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created on 7/10/16.
 */
public class TryAndErrorTest {
    @Test public void test1() {
        /*
         * Given a list/array a of n numbers > 0, return a stream of [b1, ..., bn] so that
         * a1 * b1 + ... + an * bn < another given number
         */
        int givenTotal = 27;
        int[] a = {2, 3, 5};

        class IntermediateState {
            int remainingTotal;
            int[] b;
            int[] a;
            IntermediateState(int remainingTotal, int[] b, int[] a) {
                Objects.requireNonNull(b);
                this.remainingTotal = remainingTotal;
                this.b = b;
                this.a = a;
            }

            @Override public String toString() {
                return IntStream.of(b).mapToObj(String::valueOf).collect(Collectors.joining(","));
            }

            @Override public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                IntermediateState that = (IntermediateState) o;
                return o.toString().equals(that.toString());
            }

            @Override public int hashCode() {
                return toString().hashCode();
            }
        }

        class Utils {
            private Stream<IntermediateState> f(IntermediateState is) {
                int mina = IntStream.of(is.a).min().getAsInt();
                return IntStream.range(0, is.a.length)
                        .mapToObj(i -> {
                            int[] b = Arrays.copyOf(is.b, is.a.length);
                            b[i]++;
                            int remainingTotal = is.remainingTotal - a[i];
                            return new IntermediateState(remainingTotal, b, is.a);
                        })
                        .filter(_is -> _is.remainingTotal >= mina);
            }
        }
        Utils utils = new Utils();

        Set<IntermediateState> result = new HashSet<>();
        List<IntermediateState> list = new LinkedList<>();
        list.add(new IntermediateState(givenTotal, new int[a.length], a));
        while (true) {
            int numberOfResults = result.size();
            List<IntermediateState> _list = list.stream().flatMap(utils::f).collect(Collectors.toList());
            result.addAll(list);
            result.addAll(_list);
            if (numberOfResults == result.size()) {
                break;
            }
            list.clear();
            list.addAll(_list);
        }
        result.stream().sorted((is1, is2) -> {
            for (int i = 0; i < is1.b.length; i++) {
                if (is1.b[i] < is2.b[i]) {
                    return -1;
                }
                else if (is1.b[i] > is2.b[i]) {
                    return 1;
                }
            }
            return 0;
        }).forEach(is -> System.out.println(Arrays.toString(is.b)));

        System.out.printf("27 is 0x%s\n" +
                "11011 is %d\n", Integer.toBinaryString(27), Integer.parseInt("011011", 2));
    }

    @Test public void test2() {
        /*
         * Given an array of n element [a0, ..., a(n-1)], find all the n-element combinations of them
         */
        int[] a = { 1, 3, 2, 4 };
        class IntermediateStatus {
            int[] b, a;
            IntermediateStatus(int[] _b, int[] _a) {
                Objects.requireNonNull(_b);
                Objects.requireNonNull(_a);
                b = _b;
                a = _a;
            }
            @Override public String toString() { return b == null ? "null" : Arrays.toString(b); }
        }

        class Utils {
            // pick an element in is.a and append it to is.b
            private Stream<IntermediateStatus> pickNextElement(IntermediateStatus is) {
                return Arrays.stream(is.a).mapToObj(i -> {
                    int[] b = Arrays.copyOf(is.b, is.b.length + 1);
                    b[b.length - 1] = i;
                    List<Integer> list = new ArrayList<>(Arrays.stream(is.a).boxed().collect(Collectors.toList()));
                    list.remove(new Integer(i));
                    return new IntermediateStatus(b, list.stream().mapToInt(j -> j).toArray());
                });
            }
        }
        Utils utils = new Utils();

        // TODO check if the array a is empty
        List<IntermediateStatus> list = new ArrayList<>();
        list.add(new IntermediateStatus(new int[0], a));
        do {
            List<IntermediateStatus> _list = list.stream()
                    .flatMap(utils::pickNextElement)
                    .collect(Collectors.toList());
            list.clear();
            list.addAll(_list);
        }
        while (list.get(0).a.length != 0);

        list.forEach(System.out::println);
    }
}
