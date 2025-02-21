package Methods;

public class FootballMatch extends Match {
    String LeagueName;
    float K1;
    float KX;
    float K2;
    float Ug02;
    float Ug3p;
    float K1X;
    float KX2;
    float GG;
    float NG;
    public FootballMatch(float K1, float KX, float K2, float Ug02, float Ug3p, float K1X,
                         float KX2, float GG, float NG, String HomeName, String AwayName,
                         String StartTime, String LeagueName){
        this.K1 = K1;
        this.K2 = K2;
        this.KX = KX;
        this.Ug02 = Ug02;
        this.Ug3p = Ug3p;
        this.K1X = K1X;
        this.KX2 = KX2;
        this.GG = GG;
        this.NG = NG;
        this.HomeName = HomeName;
        this.AwayName = AwayName;
        this.StartTime = StartTime;
        this.LeagueName = LeagueName;


    }

    @Override
    public String toString() {
        return "FootballMatch{" +
                "LeagueName='" + LeagueName + '\'' +
                ", K1=" + K1 +
                ", KX=" + KX +
                ", K2=" + K2 +
                ", Ug02=" + Ug02 +
                ", Ug3p=" + Ug3p +
                ", K1X=" + K1X +
                ", KX2=" + KX2 +
                ", GG=" + GG +
                ", NG=" + NG +
                ", HomeName='" + HomeName + '\'' +
                ", AwayName='" + AwayName + '\'' +
                ", StartTime='" + StartTime + '\'' +
                '}';
    }



}
