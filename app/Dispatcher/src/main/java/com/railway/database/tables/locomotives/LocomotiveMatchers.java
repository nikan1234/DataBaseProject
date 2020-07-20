package com.railway.database.tables.locomotives;

import com.railway.database.Matcher;
import com.railway.ui.window.common.entity.Locomotive;
import com.railway.ui.window.common.entity.Station;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocomotiveMatchers {

    public static class MatchByIdBeginning extends Matcher {
        @Override
        public String getCondition() {
            if (getValue() == null)
                return "";
            return "TO_CHAR(" + LocomotiveDomains.ID
                    + ") LIKE " + format(getValue().toString() + '%');

        }
    }

    public static class NumberOfFlights extends Matcher {
        @Override
        public String getCondition() {
            final String stringFormat =
                    "%s IN (SELECT locomotive_id FROM (SELECT locomotive_id, count(*) flights_count " +
                            "                 FROM LOCOMOTIVES INNER JOIN FLIGHTS_TIMETABLE USING(locomotive_id) " +
                            "                 WHERE has_cancellation = 'N' " +
                            "                 GROUP BY locomotive_id" +
                            "                 UNION " +
                            "                 SELECT L.locomotive_id, 0 AS count " +
                            "                 FROM LOCOMOTIVES L LEFT JOIN FLIGHTS F" +
                            "                 ON L.locomotive_id = F.locomotive_id " +
                            "                 WHERE F.locomotive_id IS NULL) " +
                            "          WHERE flights_count %s %s)";
            return String.format(stringFormat,
                    LocomotiveDomains.ID,
                    comparator(),
                    Objects.requireNonNullElse(getValue(), '?'));
        }
    }

    /* Ищем локомотивы, которые не имеют рейсов в заданный момент времени
     * Если рейс задерживается, это учитывается
     * Если рейс отменен, то он не рассматривается
     */
    public static class LocatedOnThisStation extends Matcher {
        @Override
        public String getCondition() {
            final String stringFormat =
                    "%s NOT IN (SELECT locomotive_id " +
                            "   FROM FLIGHTS F INNER JOIN ROUTE_DURATIONS R ON F.route_id = R.route_id" +
                            "                   LEFT JOIN CANCELLATIONS   C ON F.flight_number = C.flight_number " +
                            "                   LEFT JOIN DELAYS          D ON F.flight_number = D.flight_number " +
                            "   WHERE C.flight_number IS NULL AND D.flight_number IS NULL " +
                            "   AND TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS') BETWEEN " +
                            "   flight_date AND flight_date + route_duration / 24 " +
                            "   UNION " +
                            "   SELECT locomotive_id " +
                            "   FROM FLIGHTS F INNER JOIN ROUTE_DURATIONS R ON F.route_id = R.route_id " +
                            "                   LEFT JOIN CANCELLATIONS   C ON F.flight_number = C.flight_number" +
                            "                  INNER JOIN DELAYS          D ON F.flight_number = D.flight_number " +
                            "                  WHERE C.flight_number IS NULL " +
                            "                  AND TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS') " +
                            "                              BETWEEN flight_date + delay_duration / 24 " +
                            "                              AND flight_date + (route_duration + delay_duration) / 24)";
            return String.format(stringFormat,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(format(getValue()), "?"),
                    Objects.requireNonNullElse(format(getValue()), "?"));
        }
    }

    public static class MatchByStationArrivalDate extends Matcher {
        private String time;
        private Integer station;

        public MatchByStationArrivalDate setStation(Station station) {
            this.station = station.getId();
            return this;
        }

        public MatchByStationArrivalDate setTime(String time) {
            this.time = time;
            return this;
        }

        /* Ищем локомотивы, которые приходят на заданную станцию в заданное время
        *  Если рейс отменен, такие локомотиы не учитываются
        *  Если рейс задерживатеся, время задержки учитывается
        * */
        @Override
        public String getCondition() {
            final String stringFormat = "%s IN " +
                    "(SELECT DISTINCT F.locomotive_id " +
                            "FROM " +
                            "FLIGHT_DETAILS FD INNER JOIN FLIGHTS F   ON FD.flight_number = F.flight_number " +
                            "               LEFT JOIN DELAYS D        ON F.flight_number = D.flight_number " +
                            "               LEFT JOIN CANCELLATIONS C ON F.flight_number = C.flight_number " +
                            "WHERE D.flight_number IS NULL " +
                            "AND   C.flight_number IS NULL " +
                            "AND TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS') = " +
                            "    TO_DATE(arrival_time, 'DD-MM-YYYY HH24:MI:SS') " +
                            "AND station_id = %s " +
                            "UNION " +
                            "SELECT DISTINCT F.locomotive_id " +
                            "FROM " +
                            "FLIGHTS F LEFT JOIN CANCELLATIONS C    ON F.flight_number = C.flight_number " +
                            "          INNER JOIN FLIGHT_DETAILS FD ON F.flight_number = FD.flight_number " +
                            "          INNER JOIN DELAYS D          ON F.flight_number = D.flight_number " +
                            "WHERE C.flight_number IS NULL " +
                            "AND TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS') = " +
                            "    TO_DATE(arrival_time, 'DD-MM-YYYY HH24:MI:SS') + delay_duration / 24" +
                            "AND station_id = %s)";
            return String.format(stringFormat,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(format(time), "?"),
                    Objects.requireNonNullElse(format(station), "?"),
                    Objects.requireNonNullElse(format(time), "?"),
                    Objects.requireNonNullElse(format(station), "?"));
        }
    }


    /*
     * Проводилась подготовка к рейсу в означенный
     * интервал времени
    * */
    public static class HadInspection extends Matcher {
        private static  DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");
        private String from;
        private String to;

        public HadInspection from(LocalDate date) {
            this.from = date.format(formatter);
            return this;
        }

        public HadInspection to(LocalDate date) {
            this.to = date.format(formatter);
            return this;
        }

        @Override
        public String getCondition() {
            final String formatString =
                    "%s IN (SELECT %s " +
                           "FROM FLIGHTS_TIMETABLE INNER JOIN INSPECTIONS USING(flight_number) " +
                           "                       INNER JOIN LOCOMOTIVES USING(locomotive_id) " +
                           "                       WHERE has_cancellation = 'N' " +
                           "                       AND   inspection_date BETWEEN TO_DATE(%s, 'DD-MM-YYYY') " +
                           "                                                 AND TO_DATE(%s, 'DD-MM-YYYY'))";
            return String.format(formatString,
                    LocomotiveDomains.ID,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(format(from), "?"),
                    Objects.requireNonNullElse(format(to), "?"));
        }
    }


    /*
     * Проводился ремонт в означенное время
    * */
    public static class HadRepair extends Matcher {
        private static  DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");
        private String from;
        private String to;

        public HadRepair from(LocalDate date) {
            this.from = date.format(formatter);
            return this;
        }

        public HadRepair to(LocalDate date) {
            this.to = date.format(formatter);
            return this;
        }

        @Override
        public String getCondition() {
            final String formatString =
                    "%s IN (SELECT %s " +
                            "FROM LOCOMOTIVES INNER JOIN REPAIR USING(locomotive_id) " +
                            "WHERE repair_date BETWEEN TO_DATE(%s, 'DD-MM-YYYY') " +
                            "                      AND TO_DATE(%s, 'DD-MM-YYYY'))";
            return String.format(formatString,
                    LocomotiveDomains.ID,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(format(from), "?"),
                    Objects.requireNonNullElse(format(to), "?"));
        }
    }

    public static class MatchByRepairCount extends Matcher {
        @Override
        public String getCondition() {
            String formatString =
                    "%s IN (" +
                            "SELECT locomotive_id " +
                            "FROM (SELECT locomotive_id, count(*) count " +
                            "      FROM REPAIR " +
                            "      GROUP BY locomotive_id " +
                            "      UNION " +
                            "      SELECT L.locomotive_id, 0 AS COUNT " +
                            "      FROM LOCOMOTIVES L LEFT JOIN REPAIR R " +
                            "      ON L.locomotive_id = R.locomotive_id " +
                            "      WHERE R.locomotive_id IS NULL) " +
                            "WHERE count = %s)";
            return String.format(formatString,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(getValue(), "?"));
        }
    }

    public static class MatchByFlightsBeforeRepair extends Matcher {
        private static  DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        private Integer count;
        private String date;

        public MatchByFlightsBeforeRepair setCount(int count) {
            this.count = count;
            return this;
        }

        public MatchByFlightsBeforeRepair setDate(LocalDate date) {
            this.date = date.format(formatter);
            return this;
        }

        @Override
        public String getCondition() {
            final String formatString =
                    "%s IN" +
                            "(SELECT locomotive_id FROM ( " +
                            "   SELECT locomotive_id, count(repair_id) count " +
                            "   FROM " +
                            "   FLIGHTS_TIMETABLE INNER JOIN REPAIR USING(locomotive_id) " +
                            "   WHERE TO_DATE(arrival_time, 'DD-MM-YYYY HH:MI:SS') <= repair_date " +
                            "   AND repair_date = TO_DATE(%s, 'DD-MM-YYYY') " +
                            "   GROUP BY locomotive_id" +
                            "   UNION" +
                            "   SELECT R.locomotive_id, 0 count " +
                            "   FROM " +
                            "   FLIGHTS_TIMETABLE F RIGHT JOIN REPAIR R " +
                            "   ON F.locomotive_id = R.locomotive_id " +
                            "   WHERE F.locomotive_id IS NULL " +
                            "   AND repair_date = TO_DATE(%s, 'DD-MM-YYYY')) " +
                            "   WHERE count = %s)";
            return String.format(formatString,
                    LocomotiveDomains.ID,
                    Objects.requireNonNullElse(format(date), "?"),
                    Objects.requireNonNullElse(format(date), "?"),
                    Objects.requireNonNullElse(count, "?"));
        }
    }

    static public class MatchByAge extends Matcher {
        @Override
        public String getCondition() {
            return "TRUNC(MONTHS_BETWEEN(sysdate, entry_date) / 12, 0)"
                    + comparator() + Objects.requireNonNullElse(getValue(), '?');
        }
    }
}
