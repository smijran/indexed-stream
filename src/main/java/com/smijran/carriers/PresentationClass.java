package com.smijran.carriers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kszalkowski on 2017-06-08.
 */
public class PresentationClass {
    public final static void main(String[] blabla)
    {
        final List<String> testList = Arrays.asList("a", "b", "c");

        final Map<String, Object> usualPsiMap = new HashMap<>();
        usualPsiMap.put("a", 20);
        usualPsiMap.put("b", 40);
        usualPsiMap.put("c", 60);

        final List<LocalDate> times = Arrays.asList(LocalDate.of(2015, 2, 2),
                LocalDate.of(1987, 2, 3),
                LocalDate.of(2018, 2, 3));


    }
}
