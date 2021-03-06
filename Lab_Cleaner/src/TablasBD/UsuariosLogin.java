package TablasBD;
// Generated 09-ago-2017 11:57:13 by Hibernate Tools 3.5.0.Final

import java.util.HashSet;
import java.util.Set;

/**
 * UsuariosLogin generated by hbm2java
 */
@SuppressWarnings({"serial", "rawtypes"})
public class UsuariosLogin implements java.io.Serializable {

	private String usuario;
	private String pass;
	private int acceso;
	private Set facturas = new HashSet(0);

	public UsuariosLogin() {
	}

	public UsuariosLogin(String usuario, String pass, int acceso) {
		this.usuario = usuario;
		this.pass = pass;
		this.acceso = acceso;
	}

	public UsuariosLogin(String usuario, String pass, int acceso, Set facturas) {
		this.usuario = usuario;
		this.pass = pass;
		this.acceso = acceso;
		this.facturas = facturas;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getAcceso() {
		return this.acceso;
	}

	public void setAcceso(int acceso) {
		this.acceso = acceso;
	}

	public Set getFacturas() {
		return this.facturas;
	}

	public void setFacturas(Set facturas) {
		this.facturas = facturas;
	}

}
