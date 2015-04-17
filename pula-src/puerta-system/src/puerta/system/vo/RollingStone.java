/**
 * Created on 2010-3-12
 * WXL 2009
 * $Id$
 */
package puerta.system.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import puerta.PuertaWeb;
import puerta.support.service.Environment;
import puerta.support.service.SessionBox;
import puerta.support.utils.WxlSugar;

/**
 * 
 * @author tiyi
 * 
 */
public class RollingStone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7755737251907765370L;

	private Map<String, RollingStonePiece> pieces = WxlSugar.newLinkedHashMap();

	public static RollingStone get(SessionBox sb) {
		Map<String, Object> props = sb.getProps();
		RollingStone rs = null;
		if (props.containsKey(PuertaWeb.ROLLING_STONE)) {

			rs = (RollingStone) props.get(PuertaWeb.ROLLING_STONE);
		} else {
			rs = new RollingStone();
			props.put(PuertaWeb.ROLLING_STONE, rs);
		}
		// sb.modified();
		return rs;
	}

	public static void remove(SessionBox sb) {
		Map<String, Object> props = sb.getProps();
		props.remove(PuertaWeb.ROLLING_STONE);
	}

	/**
	 * @param sessionMgr
	 * @return
	 */
	public static RollingStone create(SessionBox sb) {
		Map<String, Object> props = sb.getProps();
		RollingStone rs = null;
		rs = new RollingStone();
		props.put(PuertaWeb.ROLLING_STONE, rs);
		// sb.modified();

		return rs;
	}

	private String purviewId, purviewName, path, purviewNo;

	public String getPath() {
		return path;
	}

	public void setPath(String namespace) {
		this.path = namespace;
	}

	public String getPurviewNo() {
		return purviewNo;
	}

	public void setPurviewNo(String purviewNo) {
		this.purviewNo = purviewNo;
	}

	public void setPieces(Map<String, RollingStonePiece> pieces) {
		this.pieces = pieces;
	}

	public String getPurviewId() {
		return purviewId;
	}

	public void setPurviewId(String purviewId) {
		this.purviewId = purviewId;
	}

	public String getPurviewName() {
		return purviewName;
	}

	public void setPurviewName(String purviewName) {
		this.purviewName = purviewName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param env
	 */
	public void add(Environment env) {
		if (StringUtils.indexOf(env.getUri(), this.path) != 0) {
			return;
		}

		RollingStonePiece rsp = RollingStonePiece.create(env);
		pieces.put(rsp.toString(), rsp);
	}

	/**
	 * @return
	 */
	public Collection<RollingStonePiece> getPieces() {
		return pieces.values();
	}

}
