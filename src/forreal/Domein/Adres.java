package forreal.Domein;

public class Adres {
    private int adres_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int reiziger_id;
    private Reiziger reiziger;

    public Adres() {
    }

    public int getAdres_id() {
        return adres_id;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger_id = reiziger.getId();
        this.reiziger = reiziger;
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

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
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
            str.append(getReiziger().getGeboortedatum());
            str.append(".");
        }

        return str.toString();
    }
}
