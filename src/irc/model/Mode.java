/**
 * 
 */
package irc.model;

/**
 * @author chkrr00k
 *
 */
public abstract class Mode{
	protected String mode;

	public Mode(String mode) {
		this.mode = mode;
	}
	public Mode() {
		this.mode = "";
	}
	public boolean adjustMode(String mode) throws BadHandlerException{
		if(mode.length() < 2){
			return false;
		}
		String factor = mode.substring(1);
		if(mode.startsWith("+")){
			if(this.mode.contains(factor)){
			}else{
				this.mode += factor;
			}
		}else if(mode.startsWith("-")){
			if(!this.mode.contains(factor)){
			}else{
				this.mode = this.mode.replace(factor, "");
			}
		}else{
			throw new BadHandlerException();
		}
		return true;
	}
	
	
}
