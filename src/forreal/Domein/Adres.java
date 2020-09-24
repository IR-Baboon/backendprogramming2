package forreal.Domein;

public class Adres {
    private int adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int reizigerId;

    private Reiziger reiziger;

    public Adres(int adres_id, String postcode, String huisnummer, String straat, String woonplaats, int reizigerId) {
        this.adres_id = adres_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reizigerId = reizigerId;
    }

    public int getAdres_id() {
        return adres_id;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public void setAdres_id(int adres_id) {
        this.adres_id = adres_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("Adres #");
        str.append(getAdres_id());
        str.append(" ");
        str.append(getStraat());
        str.append(getHuisnummer());
        str.append(", ");
        str.append(getPostcode());
        str.append(" ");
        str.append(getWoonplaats());

        if(reiziger != null){
            str.append(" en wordt bewoond door: ");
            str.append(getReiziger().getNaam());
            str.append(" en is geboren op: ");
            str.append(getReiziger().getGeboortedatum().getTime().toString());
            str.append(".");
        }

        return str.toString();
    }
}
