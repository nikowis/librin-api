package pl.nikowis.ksiazkofilia.config;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import pl.nikowis.ksiazkofilia.model.User;
import pl.nikowis.ksiazkofilia.model.UserDetailsImpl;

public class MapperConfiguration {

    public static MapperFactory mapperFactory() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.classMap(User.class, UserDetailsImpl.class)
                .mapNulls(false)
                .mapNullsInReverse(false)
                .byDefault()
                .register();

        return mapperFactory;
    }
}
