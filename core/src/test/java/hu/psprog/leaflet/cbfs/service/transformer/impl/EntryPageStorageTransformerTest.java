package hu.psprog.leaflet.cbfs.service.transformer.impl;

import hu.psprog.leaflet.api.rest.response.common.WrapperBodyDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryDataModel;
import hu.psprog.leaflet.api.rest.response.entry.EntryListDataModel;
import hu.psprog.leaflet.cbfs.domain.EntryPage;
import hu.psprog.leaflet.cbfs.domain.EntryPageKey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link EntryPageStorageTransformer}.
 *
 * @author Peter Smith
 */
@RunWith(MockitoJUnitRunner.class)
public class EntryPageStorageTransformerTest {

    private static final int PAGE_NUMBER = 2;
    private static final Long CATEGORY_ID = 3L;
    private static final String LINK_1 = "link-1";
    private static final String LINK_2 = "link-2";

    @InjectMocks
    private EntryPageStorageTransformer entryPageStorageTransformer;

    private WrapperBodyDataModel<EntryListDataModel> source;

    @Before
    public void setup() {
        source = prepareWrapper();
    }

    @Test
    public void shouldTransformWithoutCategoryID() {

        // given
        EntryPageKey key = EntryPageKey.build(PAGE_NUMBER);

        // when
        EntryPage result = entryPageStorageTransformer.transform(key, source);

        // then
        assertResult(result, false);
    }

    @Test
    public void shouldTransformWithCategoryID() {

        // given
        EntryPageKey key = EntryPageKey.build(PAGE_NUMBER, CATEGORY_ID);

        // when
        EntryPage result = entryPageStorageTransformer.transform(key, source);

        // then
        assertResult(result, true);
    }

    private void assertResult(EntryPage result, boolean categoryIDPresent) {
        assertThat(result, notNullValue());
        assertThat(result.getPage(), equalTo(PAGE_NUMBER));
        assertThat(result.getCategoryID(), categoryIDPresent ? equalTo(CATEGORY_ID) : nullValue());
        assertThat(result.getEntries().size(), equalTo(2));
        assertThat(result.getEntries().stream().anyMatch(entry -> LINK_1.equals(entry.getLink())), is(true));
        assertThat(result.getEntries().stream().anyMatch(entry -> LINK_2.equals(entry.getLink())), is(true));
    }

    private WrapperBodyDataModel<EntryListDataModel> prepareWrapper() {
        return WrapperBodyDataModel.getBuilder()
                .withBody(EntryListDataModel.getBuilder()
                        .withItem(prepareEntryDataModel(LINK_1))
                        .withItem(prepareEntryDataModel(LINK_2))
                        .build())
                .build();
    }

    private EntryDataModel prepareEntryDataModel(String link) {
        return EntryDataModel.getBuilder()
                .withLink(link)
                .build();
    }
}
