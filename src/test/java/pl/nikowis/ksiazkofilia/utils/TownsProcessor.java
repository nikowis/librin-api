package pl.nikowis.ksiazkofilia.utils;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TownsProcessor {

    public static final List<String> ACCEPTED_TYPES = Arrays.asList("miasto", "wieś", "osada");

    public static void main(String[] args) throws IOException {
        int linecount = 0, conflictcount = 0;
        Set<Location> locations = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\main\\java\\pl\\nikowis\\ksiazkofilia\\util\\urzedowy_wykaz_nazw_miejscowosci_2015.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                linecount++;
                if (linecount == 1) {
                    continue;
                }
                Location loc = new Location(line);

                if (loc.type != null && ACCEPTED_TYPES.contains(loc.type)) {
                    boolean added = locations.add(loc);
                    if (!added) {
                        System.out.println("Conflict location! " + loc);
                        conflictcount++;
                    }
                }
            }
            System.out.println("Processed " + linecount + " lines");
            System.out.println("Conflicts " + conflictcount);
            System.out.println("Locations " + locations.size());
            FileWriter f0 = new FileWriter("C:\\Users\\nikow\\Desktop\\ksiazkofilia-api\\src\\main\\java\\pl\\nikowis\\ksiazkofilia\\util\\towns_output.csv");
            String newLine = System.getProperty("line.separator");
            for(Location loc: locations) {
                f0.write(loc.toString() + newLine);
            }
            f0.close();
            System.out.println("Saved to file");
        }

    }

    @Data
    @NoArgsConstructor
    public static class Location {
        String name;
        String type;
        String commune;
        String county;
        String region;

        public Location(String line) {
            String[] split = line.split(",");
            if (split == null || split.length < 5) {
                System.out.println("incorrect line: " + line);
                return;
            }
            name = split[0];
            type = split[1];
            commune = split[2];
            county = split[3];
            region = split[4];
        }

        @Override
        public String toString() {
            return name + "," + type + "," + commune + "," + county + "," + region + ",";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return Objects.equal(name, location.name) &&
                    Objects.equal(type, location.type) &&
                    Objects.equal(commune, location.commune) &&
                    Objects.equal(county, location.county) &&
                    Objects.equal(region, location.region);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name, type, commune, county, region);
        }
    }
}
