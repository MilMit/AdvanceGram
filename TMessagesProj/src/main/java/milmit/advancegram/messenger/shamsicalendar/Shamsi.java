package milmit.advancegram.messenger.shamsicalendar;


import com.google.firebase.appindexing.builders.AlarmBuilder;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;

import java.util.Calendar;

import milmit.advancegram.messenger.AdvanceGramConfig;


//create by miladsport
public class Shamsi {
    private Calendar calendar;
    private int date;
    private int month;
    private int weekDay;
    private int year;

    public Shamsi() {
        this.calendar = Calendar.getInstance();
        calSolarCalendar();
    }

    public Shamsi(Calendar calendar2) {
        this.calendar = calendar2;
        calSolarCalendar();
    }

    private void calSolarCalendar() {
        int i = this.calendar.get(1);
        int i2 = this.calendar.get(2) + 1;
        int i3 = this.calendar.get(5);
        this.weekDay = this.calendar.get(7) - 1;
        int[] iArr = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int[] iArr2 = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
        int i4 = i % 4;
        int i5 = 79;
        if (i4 != 0) {
            int i6 = iArr[i2 - 1] + i3;
            this.date = i6;
            if (i6 > 79) {
                int i7 = i6 - 79;
                this.date = i7;
                if (i7 <= 186) {
                    if (i7 % 31 != 0) {
                        this.month = (i7 / 31) + 1;
                        this.date = i7 % 31;
                    } else {
                        this.month = i7 / 31;
                        this.date = 31;
                    }
                    this.year = i - 621;
                    return;
                }
                int i8 = i7 - 186;
                this.date = i8;
                if (i8 % 30 != 0) {
                    this.month = (i8 / 30) + 7;
                    this.date = i8 % 30;
                } else {
                    this.month = (i8 / 30) + 6;
                    this.date = 30;
                }
                this.year = i - 621;
                return;
            }
            int i9 = i6 + ((i <= 1996 || i4 != 1) ? 10 : 11);
            this.date = i9;
            if (i9 % 30 != 0) {
                this.month = (i9 / 30) + 10;
                this.date = i9 % 30;
            } else {
                this.month = (i9 / 30) + 9;
                this.date = 30;
            }
            this.year = i - 622;
            return;
        }
        int i10 = iArr2[i2 - 1] + i3;
        this.date = i10;
        if (i < 1996) {
            i5 = 80;
        }
        if (i10 > i5) {
            int i11 = i10 - i5;
            this.date = i11;
            if (i11 <= 186) {
                if (i11 % 31 != 0) {
                    this.month = (i11 / 31) + 1;
                    this.date = i11 % 31;
                } else {
                    this.month = i11 / 31;
                    this.date = 31;
                }
                this.year = i - 621;
                return;
            }
            int i12 = i11 - 186;
            this.date = i12;
            if (i12 % 30 != 0) {
                this.month = (i12 / 30) + 7;
                this.date = i12 % 30;
            } else {
                this.month = (i12 / 30) + 6;
                this.date = 30;
            }
            this.year = i - 621;
            return;
        }
        int i13 = i10 + 10;
        this.date = i13;
        if (i13 % 30 != 0) {
            this.month = (i13 / 30) + 10;
            this.date = i13 % 30;
        } else {
            this.month = (i13 / 30) + 9;
            this.date = 30;
        }
        this.year = i - 622;
    }

    public String getDesMonthYear() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMonth());
        sb.append(" ");
        sb.append(this.year);
        return String.valueOf(sb);
    }

    public String getFullDesDateTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", Integer.valueOf(this.date)));
        sb.append(" ");
        sb.append(getMonth());
        sb.append(" ");
        sb.append(String.format("%d", Integer.valueOf(this.year)));
        sb.append(" ");
        sb.append(LocaleController.getString("Saat", R.string.Saat));
        sb.append(" ");
        sb.append(getTime());
        return String.valueOf(sb);
    }

    public String getMonth() {
        switch (this.month) {
            case 1:
                return LocaleController.getString("Farvardin", R.string.Farvardin);
            case 2:
                return LocaleController.getString("Ordibehesht", R.string.Ordibehesht);
            case 3:
                return LocaleController.getString("Khordad", R.string.Khordad);
            case 4:
                return LocaleController.getString("Tir", R.string.Tir);
            case 5:
                return LocaleController.getString("Mordad", R.string.Mordad);
            case 6:
                return LocaleController.getString("Shahrivar", R.string.Shahrivar);
            case 7:
                return LocaleController.getString("Mehr", R.string.Mehr);
            case 8:
                return LocaleController.getString("Aban", R.string.Aban);
            case 9:
                return LocaleController.getString("Azar", R.string.Azar);
            case 10:
                return LocaleController.getString("Dey", R.string.Dey);
            case 11:
                return LocaleController.getString("Bahman", R.string.Bahman);
            case 12:
                return LocaleController.getString("Esfand", R.string.Esfand);
            default:
                return "";
        }
    }

    public String getNumDate() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", Integer.valueOf(this.year)));
        sb.append("/");
        sb.append(String.format("%d", Integer.valueOf(this.month)));
        sb.append("/");
        sb.append(String.format("%d", Integer.valueOf(this.date)));
        sb.append(" ");
        return String.valueOf(sb);
    }

    public String getNumDateTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", Integer.valueOf(this.year)));
        sb.append("/");
        sb.append(String.format("%d", Integer.valueOf(this.month)));
        sb.append("/");
        sb.append(String.format("%d", Integer.valueOf(this.date)));
        sb.append(" ");
        sb.append(LocaleController.getString("Saat", R.string.Saat));
        sb.append(" ");
        sb.append(getTime());
        return String.valueOf(sb);
    }

    public String getShortDesDate() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", Integer.valueOf(this.date)));
        sb.append(" ");
        sb.append(getMonth());
        return String.valueOf(sb);
    }

    public String getShortDesDateTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d", Integer.valueOf(this.date)));
        sb.append(" ");
        sb.append(getMonth());
        sb.append(" ");
        sb.append(LocaleController.getString("Saat", R.string.Saat));
        sb.append(" ");
        sb.append(getTime());
        return String.valueOf(sb);
    }

    public String getTime() {
        String str;
        StringBuilder sb;
        int i;
        boolean z = AdvanceGramConfig.is24Hours;
        int i2 = this.calendar.get(11);
        int i3 = this.calendar.get(12);
        StringBuilder sb2 = new StringBuilder();
        if (!z) {
            sb2.append(String.format("%02d", Integer.valueOf(i2 < 12 ? i2 : i2 == 12 ? 12 : i2 - 12)));
            sb2.append(":");
            sb2.append(String.format("%02d", Integer.valueOf(i3)));
            if (i2 < 12) {
                sb = new StringBuilder();
                sb.append(" ");
                i = R.string.AM;
                str = "AM";
            } else {
                sb = new StringBuilder();
                sb.append(" ");
                i = R.string.PM;
                str = "PM";
            }
            sb.append(LocaleController.getString(str, i));
            sb2.append(sb.toString());
        } else {
            sb2.append(String.format("%02d", Integer.valueOf(i2)));
            sb2.append(":");
            sb2.append(String.format("%02d", Integer.valueOf(i3)));
        }
        return String.valueOf(sb2);
    }

    public long getTimeInMillis() {
        return this.calendar.getTimeInMillis();
    }

    public String getWeekDay() {
        switch (this.weekDay) {
            case 0:
                return LocaleController.getString(AlarmBuilder.SUNDAY, R.string.Sunday);
            case 1:
                return LocaleController.getString(AlarmBuilder.MONDAY, R.string.Monday);
            case 2:
                return LocaleController.getString(AlarmBuilder.TUESDAY, R.string.Tuesday);
            case 3:
                return LocaleController.getString(AlarmBuilder.WEDNESDAY, R.string.Wednesday);
            case 4:
                return LocaleController.getString(AlarmBuilder.THURSDAY, R.string.Thursday);
            case 5:
                return LocaleController.getString(AlarmBuilder.FRIDAY, R.string.Friday);
            case 6:
                return LocaleController.getString(AlarmBuilder.SATURDAY, R.string.Saturday);
            default:
                return "";
        }
    }
}