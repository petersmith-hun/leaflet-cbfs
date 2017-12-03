package hu.psprog.leaflet.cbfs.service.impl;

import hu.psprog.leaflet.api.rest.response.common.PaginationDataModel;
import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.cbfs.config.MirroringConfiguration;
import hu.psprog.leaflet.cbfs.domain.Category;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import hu.psprog.leaflet.cbfs.persistence.CategoryDAO;
import hu.psprog.leaflet.cbfs.service.adapter.impl.CategorizedEntryPageDataAdapter;
import hu.psprog.leaflet.cbfs.service.adapter.impl.NonCategorizedEntryPageDataAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link EntryPageDataMirroringService}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryPageDataMirroringServiceTest {

    private static final int FIRST_PAGE = 1;
    private static final int MAX_PAGES = 10;

    @Mock
    private MirroringConfiguration mirroringConfiguration;

    @Mock
    private NonCategorizedEntryPageDataAdapter nonCategorizedEntryPageDataAdapter;

    @Mock
    private CategorizedEntryPageDataAdapter categorizedEntryPageDataAdapter;

    @Mock
    private CategoryDAO categoryDAO;

    @InjectMocks
    private EntryPageDataMirroringService entryPageDataMirroringService;

    @Before
    public void setup() throws IllegalAccessException {
        given(mirroringConfiguration.getFirstPage()).willReturn(FIRST_PAGE);
        Field maxPagesField = ReflectionUtils.findField(EntryPageDataMirroringService.class, "maxPages");
        maxPagesField.setAccessible(true);
        maxPagesField.setInt(entryPageDataMirroringService, MAX_PAGES);
    }

    @Test
    public void shouldLoadOnlyOnePageAndOneCategory() throws CommunicationFailureException {

        // given
        EntryPageKey firstNonCategorizedEntryPageKey = EntryPageKey.build(1, null);
        EntryPageKey firstCategorizedEntryPageKey = EntryPageKey.build(1, 1L);

        given(categoryDAO.getCategories()).willReturn(prepareCategories(1));
        given(nonCategorizedEntryPageDataAdapter.retrieve(firstNonCategorizedEntryPageKey)).willReturn(prepareWrapper(false));
        given(categorizedEntryPageDataAdapter.retrieve(firstCategorizedEntryPageKey)).willReturn(prepareWrapper(false));

        // when
        entryPageDataMirroringService.load();

        // then
        verify(nonCategorizedEntryPageDataAdapter).retrieve(firstNonCategorizedEntryPageKey);
        verify(categorizedEntryPageDataAdapter).retrieve(firstCategorizedEntryPageKey);
    }

    @Test
    public void shouldLoadMultiplePageAndMultipleCategory() throws CommunicationFailureException {

        // given
        EntryPageKey lastNonCategorizedEntryPageKey = EntryPageKey.build(5, null);
        EntryPageKey lastCategorizedEntryPageKeyCategory1 = EntryPageKey.build(3, 1L);
        EntryPageKey lastCategorizedEntryPageKeyCategory2 = EntryPageKey.build(1, 2L);
        EntryPageKey lastCategorizedEntryPageKeyCategory3 = EntryPageKey.build(2, 3L);

        given(categoryDAO.getCategories()).willReturn(prepareCategories(3));

        given(nonCategorizedEntryPageDataAdapter.retrieve(any(EntryPageKey.class))).willReturn(prepareWrapper(true));
        given(nonCategorizedEntryPageDataAdapter.retrieve(lastNonCategorizedEntryPageKey)).willReturn(prepareWrapper(false));

        given(categorizedEntryPageDataAdapter.retrieve(any(EntryPageKey.class))).willReturn(prepareWrapper(true));
        given(categorizedEntryPageDataAdapter.retrieve(lastCategorizedEntryPageKeyCategory1)).willReturn(prepareWrapper(false));
        given(categorizedEntryPageDataAdapter.retrieve(lastCategorizedEntryPageKeyCategory2)).willReturn(prepareWrapper(false));
        given(categorizedEntryPageDataAdapter.retrieve(lastCategorizedEntryPageKeyCategory3)).willReturn(prepareWrapper(false));

        // when
        entryPageDataMirroringService.load();

        // then
        verify(nonCategorizedEntryPageDataAdapter, times(5)).retrieve(any(EntryPageKey.class));
        verify(categorizedEntryPageDataAdapter, times(6)).retrieve(any(EntryPageKey.class));
    }

    @Test
    public void shouldStopLoopAfterReachingMaxIterationSteps() throws CommunicationFailureException {

        // given
        given(categoryDAO.getCategories()).willReturn(prepareCategories(1));
        given(nonCategorizedEntryPageDataAdapter.retrieve(any(EntryPageKey.class))).willReturn(prepareWrapper(true));
        given(categorizedEntryPageDataAdapter.retrieve(any(EntryPageKey.class))).willReturn(prepareWrapper(true));

        // when
        entryPageDataMirroringService.load();

        // then
        verify(nonCategorizedEntryPageDataAdapter, times(MAX_PAGES)).retrieve(any(EntryPageKey.class));
        verify(categorizedEntryPageDataAdapter, times(MAX_PAGES)).retrieve(any(EntryPageKey.class));
    }

    @Test
    public void shouldFailSilentlyOnError() throws CommunicationFailureException {

        // given
        doThrow(CommunicationFailureException.class).when(nonCategorizedEntryPageDataAdapter).retrieve(any(EntryPageKey.class));

        // when
        entryPageDataMirroringService.load();

        // then
        // silent fail
    }

    private WrapperBodyDataModel<EntryListDataModel> prepareWrapper(boolean hasNext) {
        return WrapperBodyDataModel.getBuilder()
                .withBody(EntryListDataModel.getBuilder()
                        .withItem(EntryDataModel.getBuilder().build())
                        .build())
                .withPagination(PaginationDataModel.getBuilder()
                        .withHasNext(hasNext)
                        .build())
                .build();
    }

    private List<Category> prepareCategories(int numberOfCategories) {

        List<Category> categories = new ArrayList<>();
        for (int cnt = 0; cnt < numberOfCategories; cnt++) {
            categories.add(Category.getBuilder()
                    .withId(cnt + 1)
                    .build());
        }

        return categories;
    }
}
