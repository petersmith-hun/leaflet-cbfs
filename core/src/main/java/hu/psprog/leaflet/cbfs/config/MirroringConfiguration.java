package hu.psprog.leaflet.cbfs.config;

import hu.psprog.leaflet.bridge.client.domain.OrderBy;
import hu.psprog.leaflet.bridge.client.domain.OrderDirection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Mirroring configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class MirroringConfiguration {

    @Value("${mirroring.paged.first-page}")
    private int firstPage;

    @Value("${mirroring.paged.limit}")
    private int limit;

    @Value("${mirroring.paged.order-by}")
    private OrderBy.Entry orderBy;

    @Value("${mirroring.paged.order-dir}")
    private OrderDirection orderDir;

    public int getFirstPage() {
        return firstPage;
    }

    public int getLimit() {
        return limit;
    }

    public OrderBy.Entry getOrderBy() {
        return orderBy;
    }

    public OrderDirection getOrderDir() {
        return orderDir;
    }
}
