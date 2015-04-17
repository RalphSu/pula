package puerta;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class PuertaHib {

	public static void ifYes(DetachedCriteria dc, String string,
			int enabledStatus) {

		if (enabledStatus != PuertaWeb.NONE) {
			dc.add(Restrictions.eq(string, enabledStatus == PuertaWeb.YES));
		}

	}

}
