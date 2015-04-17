/**
 * Created on 2009-12-25
 * WXL 2009
 * $Id$
 */
package puerta.support;

import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public interface PaginationSupportFactory<E> {

	PaginationSupport<E> create(List<E> items, int totalCount, int pageSize,
			int startIndex);
}
