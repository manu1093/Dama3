/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dama;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manu
 */

public final class Arbitro {
	
	private Cell moveSource;
	private Cell moveDestination;	
	private final int turn;
        private boolean pedinaMangiaDamone=false; 
	public Arbitro(int turn){
		this.resettaMossa();
                this.turn=turn;
	}
	
	public Arbitro(Arbitro a){
                        if(a.moveDestination!=null)
                            this.moveDestination=new Cell(a.moveDestination);
                        else
                            this.moveDestination=null;
                        if(a.moveSource!=null){
                            this.moveSource=new Cell(a.moveSource);
                        }else
                            this.moveSource=null;
			
                        this.turn=a.turn;
		
		
	}
        
	public void resettaMossa(){
		
			this.moveSource=null;
			
		this.resettaDestinazione();
	}
	
	public void resettaDestinazione(){
		
			this.moveDestination=null;
		
	}
	
	
	
	public boolean inseritaSource(){
		boolean b=false;
		if(this.moveSource!=null)
			b=true;
		return b;
	}
	public boolean inseritaDestinazione(){
		
			boolean b=false;
			if(this.moveDestination!=null)
				b=true;
			return b;
		
	}
	
	public void setSource(Cell c){
		this.moveSource=c;
		
	}
	public void setDestination(Cell c){
		this.moveDestination=c;
	}
	
	public Cell getSource(){
		return moveSource;
	}
	public Cell getDestination(){
		return moveDestination;
	}
	
	
	 public boolean turnoBianco(){
		if(turn%2==0)
			return true;
		else return false;
	}
	public boolean turnoNero(){
		if(turn%2==1)
			return true;
		else return false;
	}
	
	
	public boolean controlSource(Tavola t){//controlla se sorgente Ã¨ giusta
		boolean b=true;
		if(t.isEmpty(moveSource))//t[this.moveSource.getX()][this.moveSource.getY()].getText().equals(" ")
			b=false;
		
		if(!turno(t))
			b=false;				
		
		
		return b;
	}
	
	public boolean control(Tavola t){//true se si puÃ² muovere sulla destinazione scelta
		boolean b=true;
                 
		if(this.moveSource==this.moveDestination)
			b=false;
		if(!t.isEmpty(moveDestination))
			b=false;
		
		//mossa consentita solo in alto a destra o a sinistra
			
		boolean f=true;
                try{
                if(this.turnoBianco()&&t.containsPezzoBianco(this.moveSource)&&!t.containsDamone(moveSource)){
                    f=false;	
                    for(Cell c:moveSource.celleAdiacentiAlte()){
                                if(c.equals(moveDestination)){
                                    f=true;
                                }else 
                                    if(this.controlMangiata(t))
                                        f=true;

                    }
                    if(!f)b=false;
                }
                }catch(CellaVuotaException e){}
            try {
                if(this.turnoBianco()&&t.containsDamone(moveSource)&&t.containsPezzoBianco(this.moveSource)){
                    f=false;
                    for(Cell c:moveSource.celleAdiacenti()){ 
                        if(c.equals(this.moveDestination))
                            f=true;
                        else if(this.controlMangiata(t))
                            f=true;
                    }
                    if(!f)b=false;
                }
            } catch (CellaVuotaException ex) {}
            try {
                if(this.turnoNero()&&!t.containsDamone(moveSource)&&t.containsPezzoNero(moveSource)){
                    f=false;	
                    for(Cell c:moveSource.celleAdiacentiBasse()){
                        if(moveDestination.equals(c)){
                            f=true;
                        }else
                            if(this.controlMangiata(t))
                                f=true;
                        
                    }
                    if(!f)b=false;
                }
            } catch (CellaVuotaException ex) {}
            try {
                if(t.containsDamone(moveSource)&&t.containsPezzoNero(moveSource)&&this.turnoNero()){
                    f=false;
                    for(Cell c:moveSource.celleAdiacenti()){
                        if(this.moveDestination.equals(c))
                            f=true;
                        else if(this.controlMangiata(t))
                            f=true;
                    }
                    if(!f)b=false;
                }
            } catch (CellaVuotaException ex) {}
                      
					
			
		
			
		
		return b;	
				
                }
	
public Cell mangiata(Tavola t) {//true se la mossa Ã¨ una mangiata e si puÃ² fare
		
		//posso provare a mangiare
    
    Cell b=null; 
            try {
                if(t.containsDamone(this.moveSource.middleCell(this.moveDestination))&&!this.pedinaMangiaDamone)
                    if(!t.containsDamone(this.moveSource))
                        return null;
            } catch (CellaVuotaException ex) { }
    try{    
        if(t.isEmpty(moveDestination)){
            if(this.turnoBianco()&&t.containsPezzoBianco(this.moveSource)&&!t.containsDamone(moveSource)){
                
                    for(Cell c:this.moveSource.celleVicineAlte()){
                        if(this.moveDestination.equals(c)&&!isMyPedina(t.getPedina(moveSource.middleCell(moveDestination))))
                            return moveSource.middleCell(moveDestination);
                    }
                }
            }
       
    }catch(  CellaVuotaException|  NullPointerException | IllegalArgumentException e){b=null;}
   
    try{
        if(this.turnoNero()&&t.containsPedinaNera(this.moveSource)){

            if((t.isEmpty(this.moveDestination))){
                for(Cell c:this.moveSource.celleVicineBasse()){
                    if(this.moveDestination.equals(c)&&!isMyPedina(t.getPedina(moveSource.middleCell(moveDestination))))
                        return moveSource.middleCell(moveDestination);
                }
            }
        }
    }catch(   CellaVuotaException|   NullPointerException | IllegalArgumentException e){b=null;}
    
    try{
	if(t.containsDamone(this.moveSource))
            if(t.isEmpty(this.moveDestination))
                for(Cell c:this.moveSource.celleVicine())            
                    if(this.moveDestination.equals(c)&&!isMyPedina(t.getPedina(moveSource.middleCell(moveDestination))))
                        return moveSource.middleCell(moveDestination);
    }catch(  CellaVuotaException|   NullPointerException | IllegalArgumentException e){b=null;} 
    
    return b;
		


		
	}

		
	
	public boolean controlMangiata(Tavola t){		
		
		
		return this.mangiata(t)!=null;
		
	}
	private boolean turno(Tavola t){//controlla se turno  Ã¨ giusto
		boolean b=false;
                try{
		if(this.isMyPedina(t.getPedina(this.moveSource)))
			b=true;
                }catch (CellaVuotaException e){b=false;}
		return b;
	}
        
        
        
       
        public boolean isMyPedina(Pedina p) throws CellaVuotaException{
            if(p!=null){
                return (this.turnoBianco()&&p.isBianca())||(this.turnoNero()&&p.isNera());
            }else throw new IllegalArgumentException();
        }
       
        @Override
        public boolean equals(Object o){
            if(o instanceof Arbitro){
                Arbitro a= (Arbitro) o;
                return a.turn%2==this.turn%2&& a.moveDestination.equals(this.moveDestination) && a.moveSource.equals(this.moveSource);
            }
             else
                return false;
        }
        public void setPedinaMangiaDamone(boolean v){
            this.pedinaMangiaDamone=v;
        }
        public boolean getPedinaMangiaDamone(){
           return this.pedinaMangiaDamone;
        }
}
