/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public abstract class Modality implements Commands{

	protected String channel;
	protected String mode;

	public Modality() {
		super();
	}
	
	public abstract String apply();
}