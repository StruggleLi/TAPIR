package Perf;

class interfaceClass{
	String baseClass;
	String superClass;
	/**
	 * @param baseClass
	 * @param superClass
	 */
	public interfaceClass(String baseClass, String superClass) {
		super();
		this.baseClass = baseClass;
		this.superClass = superClass;
	}
	/**
	 * @return the baseClass
	 */
	public String getBaseClass() {
		return baseClass;
	}
	/**
	 * @param baseClass the baseClass to set
	 */
	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}
	/**
	 * @return the superClass
	 */
	public String getSuperClass() {
		return superClass;
	}
	/**
	 * @param superClass the superClass to set
	 */
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

}
