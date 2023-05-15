import java.util.ArrayList;
import java.util.List;

public class CityFactory {
    //creates all the cities and connectivity data for the whole game
    public static List<City> createCities(List<Disease> diseases) {
        List<City> cities = new ArrayList<>();
        City atlanta = new City(0, "Atlanta", diseases.get(0));
        City chicago = new City(1, "Chicago", diseases.get(0));
        City essen = new City(2, "Essen", diseases.get(0));
        City london = new City(3, "London", diseases.get(0));
        City madrid = new City(4, "Madrid", diseases.get(0));
        City milan = new City(5, "Milan", diseases.get(0));
        City montreal = new City(6, "Montreal", diseases.get(0));
        City newYork = new City(7, "New York", diseases.get(0));
        City paris = new City(8, "Paris", diseases.get(0));
        City sanFrancisco = new City(9, "San Francisco", diseases.get(0));
        City stPetersburg = new City(10, "St. Petersburg", diseases.get(0));
        City washington = new City(11, "Washington", diseases.get(0));

        City bangkok = new City(12, "Bangkok", diseases.get(1));
        City beijing = new City(13, "Beijing", diseases.get(1));
        City hoChiMinhCity = new City(14, "Ho Chi Minh City", diseases.get(1));
        City hongKong = new City(15, "Hong Kong", diseases.get(1));
        City jakarta = new City(16, "Jakarta", diseases.get(1));
        City manila = new City(17, "Manila", diseases.get(1));
        City osaka = new City(18, "Osaka", diseases.get(1));
        City seoul = new City(19, "Seoul", diseases.get(1));
        City shanghai = new City(20, "Shanghai", diseases.get(1));
        City sydney = new City(21, "Sydney", diseases.get(1));
        City taipei = new City(22, "Taipei", diseases.get(1));
        City tokyo = new City(23, "Tokyo", diseases.get(1));

        City bogota = new City(24, "Bogota", diseases.get(2));
        City buenosAires = new City(25, "Buenos Aires", diseases.get(2));
        City johannesburg = new City(26, "Johannesburg", diseases.get(2));
        City khartoum = new City(27, "Khartoum", diseases.get(2));
        City kinshasa = new City(28, "Kinshasa", diseases.get(2));
        City lagos = new City(29, "Lagos", diseases.get(2));
        City lima = new City(30, "Lima", diseases.get(2));
        City losAngeles = new City(31, "Los Angeles", diseases.get(2));
        City mexicoCity = new City(32, "Mexico City", diseases.get(2));
        City miami = new City(33, "Miami", diseases.get(2));
        City santiago = new City(34, "Santiago", diseases.get(2));
        City saoPaulo = new City(35, "Sao Paulo", diseases.get(2));

        City algiers = new City(36, "Algiers", diseases.get(3));
        City baghdad = new City(37, "Baghdad", diseases.get(3));
        City cairo = new City(38, "Cairo", diseases.get(3));
        City chennai = new City(39, "Chennai", diseases.get(3));
        City delhi = new City(40, "Delhi", diseases.get(3));
        City istanbul = new City(41, "Istanbul", diseases.get(3));
        City karachi = new City(42, "Karachi", diseases.get(3));
        City kolkata = new City(43, "Kolkata", diseases.get(3));
        City moscow = new City(44, "Moscow", diseases.get(3));
        City mumbai = new City(45, "Mumbai", diseases.get(3));
        City riyadh = new City(46, "Riyadh", diseases.get(3));
        City tehran = new City(47, "Tehran", diseases.get(3));


        // Atlanta edges
        atlanta.addNeighbor(chicago);
        atlanta.addNeighbor(washington);
        atlanta.addNeighbor(miami);

        // Chicago edges
        chicago.addNeighbor(sanFrancisco);
        chicago.addNeighbor(losAngeles);
        chicago.addNeighbor(mexicoCity);
        chicago.addNeighbor(atlanta);
        chicago.addNeighbor(montreal);

        // Essen edges
        essen.addNeighbor(london);
        essen.addNeighbor(paris);
        essen.addNeighbor(milan);
        essen.addNeighbor(stPetersburg);

        // London edges
        london.addNeighbor(newYork);
        london.addNeighbor(madrid);
        london.addNeighbor(paris);
        london.addNeighbor(essen);

        // Madrid edges
        madrid.addNeighbor(london);
        madrid.addNeighbor(newYork);
        madrid.addNeighbor(paris);
        madrid.addNeighbor(saoPaulo);
        madrid.addNeighbor(algiers);

        // Milan edges
        milan.addNeighbor(essen);
        milan.addNeighbor(paris);
        milan.addNeighbor(istanbul);

        // Montreal edges
        montreal.addNeighbor(washington);
        montreal.addNeighbor(newYork);
        montreal.addNeighbor(chicago);

        // New York edges
        newYork.addNeighbor(washington);
        newYork.addNeighbor(london);
        newYork.addNeighbor(madrid);
        newYork.addNeighbor(montreal);

        // Paris edges
        paris.addNeighbor(london);
        paris.addNeighbor(essen);
        paris.addNeighbor(milan);
        paris.addNeighbor(algiers);
        paris.addNeighbor(madrid);

        // San Francisco edges
        sanFrancisco.addNeighbor(chicago);
        sanFrancisco.addNeighbor(losAngeles);
        sanFrancisco.addNeighbor(manila);
        sanFrancisco.addNeighbor(tokyo);

        // St. Petersburg edges
        stPetersburg.addNeighbor(essen);
        stPetersburg.addNeighbor(istanbul);
        stPetersburg.addNeighbor(moscow);

        // Washington edges
        washington.addNeighbor(newYork);
        washington.addNeighbor(montreal);
        washington.addNeighbor(atlanta);

        // Bangkok edges
        bangkok.addNeighbor(hongKong);
        bangkok.addNeighbor(hoChiMinhCity);
        bangkok.addNeighbor(jakarta);
        bangkok.addNeighbor(chennai);
        bangkok.addNeighbor(kolkata);

        // Beijing edges
        beijing.addNeighbor(seoul);
        beijing.addNeighbor(shanghai);

        // Ho Chi Minh City edges
        hoChiMinhCity.addNeighbor(bangkok);
        hoChiMinhCity.addNeighbor(hongKong);
        hoChiMinhCity.addNeighbor(manila);
        hoChiMinhCity.addNeighbor(jakarta);

        // Hong Kong edges
        hongKong.addNeighbor(bangkok);
        hongKong.addNeighbor(hoChiMinhCity);
        hongKong.addNeighbor(manila);
        hongKong.addNeighbor(taipei);
        hongKong.addNeighbor(shanghai);
        hongKong.addNeighbor(kolkata);

        // Jakarta edges
        jakarta.addNeighbor(chennai);
        jakarta.addNeighbor(bangkok);
        jakarta.addNeighbor(hoChiMinhCity);
        jakarta.addNeighbor(sydney);


// Manila edges
        manila.addNeighbor(sanFrancisco);
        manila.addNeighbor(hongKong);
        manila.addNeighbor(hoChiMinhCity);
        manila.addNeighbor(sydney);
        manila.addNeighbor(taipei);

// Osaka edges
        osaka.addNeighbor(taipei);
        osaka.addNeighbor(tokyo);

// Seoul edges
        seoul.addNeighbor(beijing);
        seoul.addNeighbor(tokyo);
        seoul.addNeighbor(shanghai);

// Shanghai edges
        shanghai.addNeighbor(beijing);
        shanghai.addNeighbor(seoul);
        shanghai.addNeighbor(taipei);
        shanghai.addNeighbor(hongKong);

// Sydney edges
        sydney.addNeighbor(manila);
        sydney.addNeighbor(jakarta);
        sydney.addNeighbor(losAngeles);

// Taipei edges
        taipei.addNeighbor(osaka);
        taipei.addNeighbor(manila);
        taipei.addNeighbor(hongKong);
        taipei.addNeighbor(shanghai);

// Tokyo edges
        tokyo.addNeighbor(seoul);
        tokyo.addNeighbor(osaka);
        tokyo.addNeighbor(shanghai);

// Bogota edges
        bogota.addNeighbor(miami);
        bogota.addNeighbor(mexicoCity);
        bogota.addNeighbor(lima);
        bogota.addNeighbor(saoPaulo);
        bogota.addNeighbor(buenosAires);

// Buenos Aires edges
        buenosAires.addNeighbor(saoPaulo);
        buenosAires.addNeighbor(bogota);

// Johannesburg edges
        johannesburg.addNeighbor(khartoum);
        johannesburg.addNeighbor(kinshasa);

// Khartoum edges
        khartoum.addNeighbor(johannesburg);
        khartoum.addNeighbor(kinshasa);
        khartoum.addNeighbor(lagos);
        khartoum.addNeighbor(cairo);

// Kinshasa edges
        kinshasa.addNeighbor(johannesburg);
        kinshasa.addNeighbor(khartoum);
        kinshasa.addNeighbor(lagos);

// Lagos edges
        lagos.addNeighbor(khartoum);
        lagos.addNeighbor(kinshasa);
        lagos.addNeighbor(saoPaulo);

// Lima edges
        lima.addNeighbor(mexicoCity);
        lima.addNeighbor(bogota);
        lima.addNeighbor(santiago);

// Los Angeles edges
        losAngeles.addNeighbor(sanFrancisco);
        losAngeles.addNeighbor(chicago);
        losAngeles.addNeighbor(mexicoCity);
        losAngeles.addNeighbor(sydney);

// Mexico City edges
        mexicoCity.addNeighbor(losAngeles);
        mexicoCity.addNeighbor(chicago);
        mexicoCity.addNeighbor(miami);
        mexicoCity.addNeighbor(bogota);
        mexicoCity.addNeighbor(lima);

// Miami edges
        miami.addNeighbor(atlanta);
        miami.addNeighbor(mexicoCity);
        miami.addNeighbor(bogota);
        miami.addNeighbor(washington);


// Santiago edges
        santiago.addNeighbor(lima);
        santiago.addNeighbor(buenosAires);

        //Sao Paulo edges
        saoPaulo.addNeighbor(madrid);
        saoPaulo.addNeighbor(lagos);
        saoPaulo.addNeighbor(buenosAires);
        saoPaulo.addNeighbor(bogota);

// Algiers edges
        algiers.addNeighbor(madrid);
        algiers.addNeighbor(paris);
        algiers.addNeighbor(istanbul);
        algiers.addNeighbor(cairo);
// Baghdad edges
        baghdad.addNeighbor(tehran);
        baghdad.addNeighbor(karachi);
        baghdad.addNeighbor(istanbul);
        baghdad.addNeighbor(cairo);
        baghdad.addNeighbor(riyadh);

// Cairo edges
        cairo.addNeighbor(algiers);
        cairo.addNeighbor(istanbul);
        cairo.addNeighbor(baghdad);
        cairo.addNeighbor(khartoum);
        cairo.addNeighbor(riyadh);

// Chennai edges
        chennai.addNeighbor(delhi);
        chennai.addNeighbor(mumbai);
        chennai.addNeighbor(kolkata);
        chennai.addNeighbor(bangkok);


// Kolkata edges
        kolkata.addNeighbor(delhi);
        kolkata.addNeighbor(chennai);
        kolkata.addNeighbor(bangkok);
        kolkata.addNeighbor(hongKong);

        //Moscow edges
        moscow.addNeighbor(stPetersburg);
        moscow.addNeighbor(istanbul);
        moscow.addNeighbor(tehran);

// Mumbai edges
        mumbai.addNeighbor(karachi);
        mumbai.addNeighbor(delhi);
        mumbai.addNeighbor(chennai);

// Riyadh edges
        riyadh.addNeighbor(baghdad);
        riyadh.addNeighbor(cairo);
        riyadh.addNeighbor(karachi);

        //Tehran edges
        tehran.addNeighbor(moscow);
        tehran.addNeighbor(baghdad);
        tehran.addNeighbor(karachi);
        moscow.addNeighbor(delhi);

        cities.add(atlanta);
        cities.add(chicago);
        cities.add(essen);
        cities.add(london);
        cities.add(madrid);
        cities.add(milan);
        cities.add(montreal);
        cities.add(newYork);
        cities.add(paris);
        cities.add(sanFrancisco);
        cities.add(stPetersburg);
        cities.add(washington);
        cities.add(bangkok);
        cities.add(beijing);
        cities.add(hoChiMinhCity);
        cities.add(hongKong);
        cities.add(jakarta);
        cities.add(manila);
        cities.add(osaka);
        cities.add(seoul);
        cities.add(shanghai);
        cities.add(sydney);
        cities.add(taipei);
        cities.add(tokyo);
        cities.add(bogota);
        cities.add(buenosAires);
        cities.add(johannesburg);
        cities.add(khartoum);
        cities.add(kinshasa);
        cities.add(lagos);
        cities.add(lima);
        cities.add(losAngeles);
        cities.add(mexicoCity);
        cities.add(miami);
        cities.add(santiago);
        cities.add(saoPaulo);
        cities.add(algiers);
        cities.add(baghdad);
        cities.add(cairo);
        cities.add(chennai);
        cities.add(delhi);
        cities.add(istanbul);
        cities.add(karachi);
        cities.add(kolkata);
        cities.add(moscow);
        cities.add(mumbai);
        cities.add(riyadh);
        cities.add(tehran);

        return cities;
    }
}
