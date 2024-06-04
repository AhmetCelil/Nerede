package com.example.nerede;

import java.io.Serializable;

public class Etkinlik implements Serializable {

    private String key;
    private String isim;
    private String aciklama;
    private String konum;
    private String zaman;
    private String etkinlikKapasitesi;
    private String saat;
    private long timestamp;
    private String rezerveSayisi;

    public Etkinlik(String isim, String aciklama, String konum, String zaman, String etkinlikKapasitesi, String rezerveSayisi, String saat, long timestamp) {
        this.isim = isim;
        this.aciklama = aciklama;
        this.konum = konum;
        this.zaman = zaman;
        this.etkinlikKapasitesi = etkinlikKapasitesi;
        this.rezerveSayisi = rezerveSayisi;
        this.saat = saat;
        this.timestamp = timestamp;
    }

    public Etkinlik() {
    }

    public String getIsim() {
        return isim;
    }

    public String getAciklama() {
        return aciklama;
    }

    public String getKonum() {
        return konum;
    }

    public String getZaman() {
        return zaman;
    }

    public String getEtkinlikKapasitesi() {
        return etkinlikKapasitesi;
    }

    public String getSaat() {
        return saat;
    }

    public String getRezerveSayisi() {
        return rezerveSayisi;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }

    public void setEtkinlikKapasitesi(String etkinlikKapasitesi) {
        this.etkinlikKapasitesi = etkinlikKapasitesi;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

    public void setRezerveSayisi(String rezerveSayisi) {
        this.rezerveSayisi = rezerveSayisi;
    }

}
