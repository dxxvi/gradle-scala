package home

import spock.lang.Specification

/**
 * Created by ly on 6/26/16.
 * Copied from https://github.com/spockframework/spock-example/blob/master/src/test/groovy/DataDrivenSpec.groovy
 */
class DataDrivenSpec extends Specification {
    def "maximun of 2 numbers (using data pipe)"() {
        given: "2 numbers a and b"

        when: "Math.max is used to get the maximum of them"

        then: "Math.max(a, b) = c"
        Math.max(a, b) == c

        where:
        a << [3, 5, 9]
        b << [7, 4, 9]
        c << [7, 5, 9]
    }

    def "maximun of 2 numbers (using data table)"() {
        given: "2 numbers #a and #b"

        when: "Math.max is used to get the maximum of them"

        then: "Math.max(a, b) = c"
        Math.max(a, b) == c

        where:
        a | b || c
        3 | 7 || 7
        5 | 4 || 5
        9 | 9 || 9
    }
}
