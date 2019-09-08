package com.example.beacon.vdf.infra.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    public static ZonedDateTime getCurrentTrucatedZonedDateTime(){
        ZonedDateTime now = ZonedDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .withZoneSameInstant((ZoneOffset.UTC).normalized());
        return now;
    }

    public static long datetimeToMilli(ZonedDateTime time){
        return time.toInstant().toEpochMilli();
    }

    public static ZonedDateTime longToLocalDateTime(String data){
        Long millis = new Long(data);
        if (data.length() == 10){
            millis = millis*1000;
        }

        // atual
        ZonedDateTime localDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis),
                ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);

        return localDateTime;
    }


    public static String getTimeStampFormated(ZonedDateTime timestamp){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
        String format = timestamp.withZoneSameInstant((ZoneOffset.UTC).normalized()).format(dateTimeFormatter);
        return format;
    }

    public static long getMinutesForNextRun(ZonedDateTime dateTimeActual, ZonedDateTime nextRun){
        return ChronoUnit.MINUTES.between(dateTimeActual, nextRun);
    }

    public static ZonedDateTime getTimestampOfNextRun(ZonedDateTime dateTime){

        ZonedDateTime zonedDateTime30 = dateTime.withMinute(30);
        ZonedDateTime zonedDateTimeNextHour = dateTime.plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.HOURS);

        long between1 = ChronoUnit.MINUTES.between(dateTime, zonedDateTime30);
        long between2 = ChronoUnit.MINUTES.between(dateTime, zonedDateTimeNextHour);

        ZonedDateTime doReturn = null;

        if (between1 < 0){
            return zonedDateTimeNextHour.truncatedTo(ChronoUnit.MINUTES);
        }

        if (between1 < between2){
            doReturn = zonedDateTime30;
        }

        if (between1 > between2){
            doReturn = zonedDateTimeNextHour;
        }

        return doReturn.truncatedTo(ChronoUnit.MINUTES);
    }

}
