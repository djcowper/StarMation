


import java.util.Date;

public class RaDecToAltAz {

    private double ra, dec, lat, lon;
    private Date date;
    private double jd;
    double az, alt;

    public RaDecToAltAz(double lat, double lon, double ra, double dec, Date date) {
        this.ra = toRad((360.0 / 24.) * ra);
        this.dec = toRad(dec);
        this.date = date;
        this.jd = dateToJulianDate(this.date);
        this.lat = toRad(lat);
        this.lon = toRad(lon);
        // this.alt = raDecToAlt(this.ra, this.dec,
        // this.jd, this.lat, this.lon);
        // this.az = raDecToAz(this.ra, this.dec,
        // this.jd, this.lat, this.lon, this.alt);
        raDecToAltAz(this.lat, this.lon);
    }

    private double toRad(double deg) {
        return deg * Math.PI / 180.0;
    }

    // Converts a date to Julian Date
    private static double dateToJulianDate(Date date) {
        long milliseconds = date.getTime();
        double julianDate = milliseconds / 86400000.0 + 2440587.5;
        return julianDate;
    }

    // Converts RA and Dec to Alt
    private static double raDecToAlt(double ra, double dec, double julianDate, double latitude, double longitude) {
        double hourAngle = julianDateToHourAngle(julianDate, longitude);

        // a = (Math.asin(Math.sin(lat) * Math.sin(dec) + Math.cos(lat) * Math.cos(dec)
        // * Math.cos(H)));
        // double altitude = Math.asin(Math.sin(latitude) * Math.sin(declination) +
        // Math.cos(latitude) * Math.cos(declination) * Math.cos(LHA));

        double alt = Math
                .asin(Math.sin(dec) * Math.sin(latitude) + Math.cos(dec) * Math.cos(hourAngle) * Math.cos(latitude));
        return alt;
    }

    // Converts RA and Dec to Az
    private static double raDecToAz(double ra, double dec, double julianDate, double latitude, double longitude,
            double altitude) {
        double hourAngle = julianDateToHourAngle(julianDate, longitude);
        double az = Math
                .acos((Math.sin(dec) * Math.cos(latitude) - Math.cos(dec) * Math.cos(hourAngle) * Math.sin(latitude))
                        / Math.cos(altitude));
        return az;
    }

    // Converts Julian Date to Hour Angle
    private static double julianDateToHourAngle(double julianDate, double longitude) {
        double hourAngle = (julianDate - 2451545.0) / 36525.0 * 24.0 - longitude / 15.0;
        return hourAngle;

    }

    public double getAlt() {
        return alt;
    }

    public double getAz() {
        return az;
    }

    public void raDecToAltAz(double lat, double lon) {
        double ra = this.ra;
        double dec = this.dec;
        double jd_ut = this.jd;

        // Meeus 13.5 and 13.6, modified so West longitudes are negative and 0 is North
        double gmst = greenwichMeanSiderealTime(jd_ut);
        double localSiderealTime = (gmst + lon) % (2 * Math.PI);

        double H = (localSiderealTime - ra);
        // if (H < 0) {
        // H += 2 * Math.PI;
        // }
        // if (H > Math.PI) {
        // H = H - 2 * Math.PI;
        // }

        double az = (Math.atan2(Math.sin(H), Math.cos(H) * Math.sin(lat) - Math.tan(dec) * Math.cos(lat)));
        // (Math.atan2(Math.sin(dec) - Math.sin(lat)*Math.cos(H), Math.sin(H) *
        // Math.cos(lat)));
        //
        double a = (Math.asin(Math.sin(lat) * Math.sin(dec) + Math.cos(lat) * Math.cos(dec) * Math.cos(H)));
        az -= Math.PI;
        if (az < 0) {
            az += 2 * Math.PI;
        }
        this.alt = a;
        this.az = az;

    }

    public double greenwichMeanSiderealTime(double jd) {
        // "Expressions for IAU 2000 precession quantities" N. Capitaine1,P.T.Wallace2,
        // and J. Chapront
        double t = ((jd - 2451545.0)) / 36525.0;

        double gmst = this.earthRotationAngle(jd)
                + (0.014506 + 4612.156534 * t + 1.3915817 * t * t - 0.00000044 * t * t * t - 0.000029956 * t * t * t * t
                        - 0.0000000368 * t * t * t * t * t) / 60.0 / 60.0 * Math.PI / 180.0; // eq 42
        gmst %= 2 * Math.PI;
        if (gmst < 0)
            gmst += 2 * Math.PI;

        return gmst;
    }

    public double earthRotationAngle(double jd) {
        // IERS Technical Note No. 32

        double t = jd - 2451545.0;
        double f = jd % 1.0;

        double theta = 2 * Math.PI * (f + 0.7790572732640 + 0.00273781191135448 * t); // eq 14
        theta %= 2 * Math.PI;
        if (theta < 0)
            theta += 2 * Math.PI;

        return theta;

    }
}
