package datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entidades.Carrera;

public class Dt_carrera {

	poolConexion pc = poolConexion.getInstance(); 
	Connection c = null;
	private ResultSet rsCarrera = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	public void llena_rsCarrera(Connection c){
		try{
			ps = c.prepareStatement("SELECT * FROM gc_mcgofe.carrera WHERE estado<>3;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
			rsCarrera = ps.executeQuery();
		}
		catch (Exception e){
			System.out.println("DATOS: ERROR EN LISTAR CARRERAS "+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	//Metodo para visualizar usuarios registrados y activos
	public ArrayList<Carrera> listaCarActivos(){
		ArrayList<Carrera> listCar = new ArrayList<Carrera>();
		try{
			c = poolConexion.getConnection(); //obtenemos una conexion del pool
			ps = c.prepareStatement("SELECT * FROM gc_mcgofe.carrera WHERE estado<>3;", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = ps.executeQuery();
			while(rs.next()){
				Carrera car = new Carrera(); //instanciamos a rol
				car.setId_carrera(rs.getInt("id_carrera"));
				car.setNombre_carrera(rs.getString("nombre_carrera"));
				car.setEstado(rs.getInt("estado"));
				car.setId_departamento(rs.getInt("id_departamento"));
				listCar.add(car);
			}
		}
		catch (Exception e){
			System.out.println("DATOS: ERROR EN LISTAR carreras: "+ e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
				if(c != null){
					poolConexion.closeConnection(c);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return listCar;
	}
	
	public boolean addCarrera(Carrera ca){
		boolean guardado = false;
		
		try{
			c = poolConexion.getConnection();
			this.llena_rsCarrera(c);
			this.rsCarrera.moveToInsertRow();
			rsCarrera.updateString("nombre_carrera", ca.getNombre_carrera());
			rsCarrera.updateInt("id_departamento", ca.getId_departamento());
			rsCarrera.updateInt("estado", 1);
			rsCarrera.insertRow();
			rsCarrera.moveToCurrentRow();
			guardado = true;
		}
		catch (Exception e) {
			System.err.println("ERROR AL GUARDAR facultad: "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				if(rsCarrera != null){
					rsCarrera.close();
				}
				if(c != null){
					poolConexion.closeConnection(c);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return guardado;
	}

}
