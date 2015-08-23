/**
 * 
 */
package pula.sys.domains;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Liangfei
 *
 */
public class StudentCourse {

    @JsonProperty
    private TimeCourse course;

    @JsonProperty
    private List<TimeCourseOrder> orders = new ArrayList<TimeCourseOrder>();

    @JsonProperty
    private List<TimeCourseOrderUsage> orderUsages = new ArrayList<TimeCourseOrderUsage>();

    /**
     * 0 - comes from order <br/>
     * 1 - comes form non-ordered usage
     */
    @JsonProperty
    private int sourceType = -1;

    public TimeCourse getCourse() {
        return course;
    }

    public void setCourse(TimeCourse course) {
        this.course = course;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public List<TimeCourseOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<TimeCourseOrder> order) {
        this.orders = order;
    }

    @JsonIgnore
    public void addOrder(TimeCourseOrder order) {
        this.orders.add(order);
    }

    public List<TimeCourseOrderUsage> getOrderUsages() {
        return orderUsages;
    }

    public void setOrderUsages(List<TimeCourseOrderUsage> orderUsage) {
        this.orderUsages = orderUsage;
    }

    @JsonIgnore
    public void addOrderUsage(TimeCourseOrderUsage usage) {
        this.orderUsages.add(usage);
    }

}
