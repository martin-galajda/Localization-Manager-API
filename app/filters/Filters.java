package filters;

import play.filters.csrf.CSRFFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

/**
 * Created by martin on 11/14/16.
 */
public class Filters extends DefaultHttpFilters {
	@Inject
	public Filters(CSRFFilter csrfFilter) {
		super(csrfFilter);
	}

}
