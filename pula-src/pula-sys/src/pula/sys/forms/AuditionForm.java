package pula.sys.forms;

import org.apache.commons.lang.StringUtils;

import pula.sys.domains.Audition;
import pula.sys.domains.SysCategory;

public class AuditionForm extends Audition {

	private String resultId;

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public Audition toAudition() {
		Audition obj = new Audition();
		obj.setAge(this.getAge());
		obj.setBranch(this.getBranch());
		obj.setClosed(!StringUtils.isEmpty(resultId));
		obj.setComments(this.getComments());
		obj.setContent(this.getContent());
		obj.setId(this.getId());
		obj.setOwner(this.getOwner());
		obj.setParent(this.getParent());
		obj.setPhone(this.getPhone());
		obj.setPlan1(this.getPlan1());
		obj.setPlan2(this.getPlan2());
		obj.setPlan3(this.getPlan3());
		obj.setPlan4(this.getPlan4());
		obj.setPlan5(this.getPlan5());
		obj.setResult(SysCategory.create(this.resultId));
		obj.setStudent(this.getStudent());
		return obj;
	}

	public boolean isEmpty() {
		return id == 0
				&& allEmpty(getAge(), resultId, comments, content, parent,
						phone, plan1, plan2, plan3, plan4, plan5, student);
	}

	private boolean allEmpty(String... s) {
		boolean e = true;
		for (String ss : s) {
			e = e && StringUtils.isEmpty(ss);
		}
		return e;
	}

}
