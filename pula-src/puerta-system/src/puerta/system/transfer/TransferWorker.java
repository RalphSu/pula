/**
 * Created on 2007-1-1 08:49:22
 *
 * DiagCN.COM 2004-2006
 * $Id: TransferWorker.java,v 1.2 2007/01/09 03:18:17 tiyi Exp $
 */
package puerta.system.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import puerta.support.mls.Mls;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.DictLimb;
import puerta.system.po.Module;
import puerta.system.po.OrderNoRule;
import puerta.system.po.Parameter;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;
import puerta.system.po.Purview;
import puerta.system.po.PurviewToRequestUri;
import puerta.system.po.RequestUri;
import puerta.system.po.Shortcut;

/**
 * 
 * @author tiyi
 * 
 */
public class TransferWorker {

	private static Map<String, JDOMTransfer<? extends Object>> transfersMap = new HashMap<String, JDOMTransfer<? extends Object>>();

	public static void register(String name,
			JDOMTransfer<? extends Object> transfer) {
		transfersMap.put(name, transfer);
	}

	static {
		register(RequestUri.class.getName(), new RequestUriTransfer());
		register(Purview.class.getName(), new PurviewTransfer());
		register(Module.class.getName(), new ModuleTransfer());
		register(PurviewToRequestUri.class.getName(), new PurviewToRequestUriTransfer());
		register(AppField.class.getName(), new AppFieldTransfer());
		register(Parameter.class.getName(), new ParameterTransfer());
		register(ParameterFolder.class.getName(), new ParameterFolderTransfer());
		register(ParameterPage.class.getName(), new ParameterPageTransfer());
		register(Shortcut.class.getName(), new ShortcutTransfer());
		register(OrderNoRule.class.getName(), new OrderNoRuleTransfer());
		register(DictLimb.class.getName(), new DictLimbTransfer());
	}

	private static JDOMTransfer<? extends Object> getTransfer(String name) {

		return transfersMap.get(name);

	}

	/**
	 * @param l
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Element> transferElements(List<T> l, String s) {
		JDOMTransfer<T> transfer = (JDOMTransfer<T>) getTransfer(s);
		if (transfer == null) {
			Mls.raise("platform.setting.cannotFoundTransferWork", s.toString());
		}

		List<Element> el = new ArrayList<Element>();
		for (Iterator<T> iter = l.iterator(); iter.hasNext();) {
			T obj = iter.next();
			Element ele = transfer.getElement(obj);
			if (ele == null)
				continue;
			el.add(ele);
		}

		return el;
	}

	/**
	 * @param root
	 * @param name
	 * @return
	 */

	public static <T> List<T> transferObjects(Element root, String string) {
		return getObjects(root, string);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getObjects(Element element, String clazz) {
		JDOMTransfer<? extends Object> transfer = getTransfer(clazz);
		List<? extends Object> elements = element.getChildren(transfer
				.getElementName());
		List<Object> list = new ArrayList<Object>();
		for (Iterator<? extends Object> iter = elements.iterator(); iter
				.hasNext();) {
			Element e = (Element) iter.next();
			Object obj = transfer.getObject(e);
			list.add(obj);
		}
		return (List<T>) list;
	}
}
