package TablasBD;
// Generated 25-jul-2017 18:49:21 by Hibernate Tools 3.5.0.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Clientes generated by hbm2java
 */
@SuppressWarnings({"rawtypes", "serial"})
public class Clientes implements java.io.Serializable {

	private String dni;
	private String nombre;
	private String apellido;
	private Date fecha;
	private String telefono;
	private String movil;
	private String direccion;
	private String poblacion;
	private String provincia;
	private String email;
	
	private Set facturas = new HashSet(0);
	private Set percheros = new HashSet(0);

	public Clientes() {
	}

	public Clientes(String dni, String nombre, String apellido) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public Clientes(String dni, String nombre, String apellido, Date fecha, String telefono, String movil,
			String direccion, String poblacion, String provincia, String email, Set facturas, Set percheros) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fecha = fecha;
		this.telefono = telefono;
		this.movil = movil;
		this.direccion = direccion;
		this.poblacion = poblacion;
		this.provincia = provincia;
		this.email = email;
		this.facturas = facturas;
		this.percheros = percheros;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMovil() {
		return this.movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set getFacturas() {
		return this.facturas;
	}

	public void setFacturas(Set facturas) {
		this.facturas = facturas;
	}

	public Set getPercheros() {
		return this.percheros;
	}

	public void setPercheros(Set percheros) {
		this.percheros = percheros;
	}

}
