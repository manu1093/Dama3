/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dama;


import javax.swing.JOptionPane;
import albero.GenericArbitroTree;
import albero.GenericTavolaTree;
import albero.Node;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Engine {
	private Arbitro ar;
	private boolean mangiato;	
	private int turno;
        private int damonePedina;
        private int nTavoleUguali;
        private static Stack <Tavola> history;        
        private ArrayList<Arbitro> pm;
       
	public Engine(int t){
            damonePedina=3;
            nTavoleUguali=3;
            ar=new Arbitro(t);
            turno=t;
            mangiato=false;
            if(history==null)
                history=new Stack<>();
            pm=new ArrayList<>();
	}
       
	
	
	public int receivedinput(Tavola t,Cell ce) {//0 mezza mossa 1 mossa intera //stampa tavolata
		
		
		
                if(!(ar.inseritaSource())){
                        //gestione stallo(se esiste il metodo c'è)                        
                                         
                        ar.setSource(ce);
                        //f=false;
                        if(!ar.controlSource(t)){
                                ar.resettaMossa();
                               
                                
                        }else
                                return 0;//seleziona la pedina col giallo
                }
                if(mangiato){//sono in una fase della magiata
                    ar.setDestination(ce);
                    
                    boolean b=false;
                    for(Arbitro c:pm)//pedine con mangiata massima
                                if(c!=null)
                                    if(ar.getDestination().equals(c.getDestination())){
                                        b=true;
                                        
                                     }
                            if(pm.get(0)!=null&&!b){
                                JOptionPane.showMessageDialog(null,"obbligatorio proseguire con la mangiata");									
                                
                                ar.resettaMossa();
                                return 0;
                            }
                    
                }
                if(ar.inseritaSource()&&!ar.inseritaDestinazione()){
                        
                    
                       
                        ar.setDestination(ce);                       
                          
                         
                          
                            if(!ar.control(t)){
                                JOptionPane.showMessageDialog(null,"mossa non valida");
                                ar.resettaMossa();
                                return 2;
                            }
                            boolean b=false;
                    
                        if(t.getNPedine()==6)
                            System.out.println("");
                    
                            for(Arbitro c:this.mangiabiliP(t))//pedine con mangiata massima
                                if(c!=null)
                                    if(ar.getSource().equals(c.getSource())&&ar.getDestination().equals(c.getDestination())){
                                        b=true;
                                        
                                     }
                            if(this.mangiabiliP(t).get(0)!=null&&!b){
                                JOptionPane.showMessageDialog(null,"obbligatorio mangiare");									
                                
                                ar.resettaMossa();
                                return 2;
                            }
                            	
                        
                }

                						
                if(ar.inseritaSource()&&ar.inseritaDestinazione()){
                        Engine.history.push(new Tavola(t));
                        
                        int r;
                        if(ar.controlMangiata(t)){
                            r=gestisciMangiata(t,this.ar);
                        }else{						
                                t.swapCelle(ar.getSource(), ar.getDestination());
                                if(avvenutaPromozione(ar,t))
                                        try {
                                            t.promuoviPedina(cellaPedinaPromossa(ar,t));
                                } catch (CellaVuotaException ex) {
                                    System.out.println("non trovo pedina da promouvere"); 
                                }
                                
                                ar.resettaMossa();
                                r=1;
                        }
   
                       
                        return r;
                }
                return 9;		


        }
	private boolean avvenutaPromozione(Arbitro ar,Tavola t){//metti nell'arbitro
		return cellaPedinaPromossa(ar,t)!=null;
	}
	private Cell cellaPedinaPromossa(Arbitro ar,Tavola t){try {
            //metti nell'arbitro
            if(t.getPedina(ar.getDestination()).isDamone())
                return null;
            } catch (CellaVuotaException ex) {    }
                if(this.turnoBianco())
                    if(ar.getDestination().getY()==0)
                        return ar.getDestination();
            
			if(ar.turnoNero())
				if(ar.getDestination().getY()==7)
					return ar.getDestination();
		
		
		return null;
	}
	
        private ArrayList<Arbitro> mangiabiliP(Tavola t) {//torna tutte le celle che possono mangiare
		Arbitro arr=new Arbitro(ar);
               
                
                ArrayList <Node> mp=new ArrayList<>();//mangiate possibili
			for(Cell c:t){//guarda tutte le celle e trova le mangiate massime per ogni pedina
                            try{
				if(arr.isMyPedina(t.getPedina(c))){//è del mio turno					
                                    Arbitro temp=new Arbitro(arr);
                                    temp.setSource(c);
                                    temp.setDestination(c);
                                    Node root=new Node(temp,0);//deve essere 0;            
                                    this.creaAlberoMangiatePossibili(t, root);                                    
                                    if(!root.getChildren().isEmpty()){//può mangiare
                                        ArrayList<Node>a=this.candidatiAllaMaggioreMangiata(t, root);
                                        a=this.nodiMiglioriPiuAlti(a);
                                        mp.addAll(a);
                                    }
                                    
                                    
				}
                            }catch(IllegalArgumentException e){} catch (CellaVuotaException ex) {
                        Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            //troviamo le magiate migliori
                           
                            
                        }
                         Node p=new Node(null,0);
                            for(Node n:mp)
                                p.addFiglio(n);
                            ArrayList<Arbitro> mm=new ArrayList<>();
                             ArrayList<Node>a=this.candidatiAllaMaggioreMangiata(t, p);
                            
                            for(Node n:this.nodiMiglioriPiuAlti(a)){
                                GenericArbitroTree g=new GenericArbitroTree(n);
                                mm.add(g.getArbitro());
                            }
		return mm;
					
	}
	private ArrayList<Node> candidatiAllaMaggioreMangiata(Tavola t,Node root) {//torna un array di nodi con massima mangiata
            ArrayList<Node> r=new ArrayList();            
            int v=0;
            int max=0;
            Node nMax=root;//nodo con mangiata massima
           
            for(Node n:root.getChildren()){//candidati alle mangiate migliori                                 
                   if(max<n.getId()){
                       max=n.getId();
                       nMax=n;
                   }
               }
            boolean pu=true;
            for(Node n:root.getChildren()){
                GenericArbitroTree g=new GenericArbitroTree(n);                
                
                    if(n.getId()==max){
                        try {
                        if(t.getPedina(g.getArbitro().getSource().middleCell( g.getArbitro().getDestination())).isDamone()){
                            v++;//conto i nodi con punteggio massimo
                            r.add(n);//salvo i nodi con punteggio massimo
                            pu=false;
                        }
                            
                        
                } catch (CellaVuotaException ex) {
                   
                }
            }
            }
            if(pu){
                
                for(Node n:root.getChildren())
                    if(n.getId()==max){
                        ArrayList<Node>a=this.candidatiAllaMaggioreMangiata(t,n);
                        r.addAll(a);
                    }
                if(root.getChildren().isEmpty())
                    r.add(root);
            }
            return r;
        }
        //torna il padre del nodo più basso da abbinare a l'altro
        private ArrayList<Node> nodiMiglioriPiuAlti(ArrayList<Node> p){//era Qualcosa
            ArrayList a=new ArrayList<>();
            int min=3000;
            for(Node n:p){
                if(n.getNodeDepth()<min)
                    min=n.getNodeDepth();
            }
            for(Node n:p){
                if(min==n.getNodeDepth()){
                    Node root=n;
                    while(root.getNodeDepth()>1)
                        root=root.getFatherN();
                    a.add(root);
                }
            }
            return a;
        }
        

	private GenericArbitroTree creaAlberoMangiatePossibili(Tavola t,Node root) {//per ora torna una pedina qualsiasi ache può mangiare            
		GenericArbitroTree g=new GenericArbitroTree(root);
                Cell d=g.getArbitro().getDestination();                 //se ci sono mette nell'arbitro l'ultima mangiata
                Arbitro temp=new Arbitro(g.getArbitro());
                temp.setSource(d);
                for(Cell c:d.celleVicine()){//se non ci sno celle che possono mangiare torna arbitro equivalente;
                    
                    temp.setDestination(c);
                    if(temp.controlMangiata(t)){
                        
                        int v=0;
                         try {
                            
                                v=this.getPunteggio(t.getPedina(temp.mangiata(t)));
                            
                            } catch (CellaVuotaException ex) { }
                         Node child=new Node(new Arbitro(temp),v);
                        try{
                        root.addFiglio(child);
                        }catch(NullPointerException e){System.out.println("problema");}
                        Tavola t2=new Tavola(t);
                       
                        t2.removePezzo(temp.mangiata(t2));
                        
                        
                        t2.swapCelle(temp.getSource(), temp.getDestination());//l'arbitro si collega alle celle di t e non t2
                       // temp.setSource(temp.getDestination());
                        
                        creaAlberoMangiatePossibili(t2,child); 
                       
                
                
                
		
	}
                    
                       
                }
                
               
               
                int max=0;
                if(!root.getChildren().isEmpty()){
                        for(Node a:root.getChildren()){
                           if(a.getId()>max)
                               max=a.getId();
                       }
                        root.setId(max+root.getId());
                        
                }
               return g;
                
               
        }
         
	private int gestisciMangiata(Tavola t,Arbitro ar){
            
            Cell c=ar.mangiata(t);
            
                t.removePezzo(c);//togli le pedine		
           
            t.swapCelle(ar.getSource(), ar.getDestination());
            
            Arbitro temp=new Arbitro(ar);
            temp.setSource(ar.getDestination());
            Node root=new Node(temp,0);//deve essere 0;            
            this.creaAlberoMangiatePossibili(t, root);
            
            
            if(!root.getChildren().isEmpty()){//la pedina può mangiare ancora
                    pm.clear();
                    ar.setSource(ar.getDestination());
                    ar.resettaDestinazione();
                    for(Node n:nodiMiglioriPiuAlti(this.candidatiAllaMaggioreMangiata(t, root)))
                        this.pm.add(new GenericArbitroTree(n).getArbitro());
                    
                    mangiato=true;
                    return 0;
            }else{
                    if(avvenutaPromozione(ar,t))
                            try {
                                t.promuoviPedina(cellaPedinaPromossa(ar,t));//cambia pedina incriminata in damone
                    } catch (CellaVuotaException ex) { }//cambia pedina incriminata in damone
                    
                    ar.resettaMossa();
                    mangiato=false;
                    return 1;
            }
            
	}
		
		
	 public boolean turnoBianco(){
            return turno%2==0;
	}
	public boolean turnoNero(){
            return turno%2==1;
	}
	public void nextTurn(){
            turno++;
            
        }
	public String getTurnoToString(){
		
		if(turno%2==0)
			return "turno del bianco";
		else
			return "turno del nero";
	}
	public Arbitro getArbitro(){
            return this.ar;
        }
        
        private int getPunteggio(Pedina p){
            
            if(p.isDamone())
                return this.damonePedina*10;
            else
                return 10;
        }
        public Tavola mossaPc(Tavola t){
            
            Engine.history.push(t);
            Node root=new Node(t,0);
            this.creaAlberoDelleTavole(root, 2);
            Tavola m=this.calcolaMossa(root);
            turno=1;
           
            
            
            return m;
            
        }
        private Node creaAlberoDelleTavole(Node root,int depth){
            if(depth==0)//da mettere nelle chiamate per rendre il progeamma più veloce
                return root;
            GenericTavolaTree g=new GenericTavolaTree(root);
            Tavola t=g.getTavola();
            for(Cell s:t){
                try {
                    if(this.isMyPedina(t.getPedina(s))){
                        for(Arbitro a:this.mangiabiliP(t))
                            try{//tutte le mangiate possibili
                                if(a.getSource().equals(s)){
                                    ArrayList <Node> b= new ArrayList<>();//buttalo fuori prima
                                    b.add(new Node(a,0));                                
                                    Node n=gestioneMangiate(root,b,t);
                                    this.nextTurn();
                                    this.creaAlberoDelleTavole(n,depth-1);
                                    this.nextTurn();
                                }
                            }catch(NullPointerException e){
                                for(Cell d:s.celleAdiacenti()){//mossa normale
                                    Arbitro r=new Arbitro(this.turno);
                                    r.setSource(s);
                                    r.setDestination(d);
                                    if(r.control(t)){
                                        Tavola t2=new Tavola(t);
                                        t2.swapCelle(s, d);
                                        if(this.avvenutaPromozione(r,t2))
                                            t2.promuoviPedina(this.cellaPedinaPromossa(r,t2));
                                        Node n=new Node(t2,this.punteggioTavola(t2));
                                        root.addFiglio(n);
                                       
                                        this.nextTurn();                                        
                                        this.creaAlberoDelleTavole(n,depth-1);
                                        this.nextTurn();
                                    }
                                }
                            
                            }
                    }
                } catch (CellaVuotaException ex) {
                    
                }
            }
            return root;
        }
        public boolean isMyPedina(Pedina p) throws CellaVuotaException{
            if(p!=null){
                return (this.turnoBianco()&&p.isBianca())||(this.turnoNero()&&p.isNera());
            }else throw new IllegalArgumentException();
        }
        private Node gestioneMangiate(Node root,ArrayList<Node> mangiate,Tavola t){//crea un albero delle tavole in caso di mangiata
            for(Node h:mangiate){
                Arbitro a=new GenericArbitroTree(h).getArbitro();
                Tavola t2=new Tavola(t);
                t2.swapCelle(a.getDestination(), a.getSource());
                t2.removePezzo(a.mangiata(t));
                Node n=new Node(a,0);
                this.creaAlberoMangiatePossibili(t2,n);
                if(!n.getChildren().isEmpty()){//si può procedere conm la mangiata
                    ArrayList <Node> b= this.candidatiAllaMaggioreMangiata(t2,n);
                    ArrayList <Node> c= this.nodiMiglioriPiuAlti(b);
                    return gestioneMangiate(root,c,t2);
                }else{//mangiata finita
                    if(this.avvenutaPromozione(a,t2))
                        try {
                            t2.promuoviPedina(this.cellaPedinaPromossa(a,t2));
                    } catch (CellaVuotaException ex) {}
                    Node n2=new Node(t2,this.punteggioTavola(t2));
                    root.addFiglio(n2);
                    return n2;
                }
            }
            return null;
            
            
        }
        
        private int punteggioTavola(Tavola t){//il turno deve essere giusto
            int r=0;
            for(Cell c:t){
                try {
                    if(this.turnoNero()){
                        if(this.isMyPedina(t.getPedina(c))){
                            if(c.centro())
                                r+=5;
                            else
                                r+=3;
                            if(t.getPedina(c).isDamone())
                                r+=30;
                            else
                                r+=10;

                        }else{
                            if(t.getPedina(c).isDamone())//conta se mangia pedine
                                r-=60;
                            else
                                r-=20;
                                
                            
                        }
                        if(controlPatta(t))
                            r=-3000;
                    }

                    } catch (CellaVuotaException ex) {

                    }
                if(this.turnoBianco())
                    try {
                        if(this.isMyPedina(t.getPedina(c))){
                          if(t.getPedina(c).isDamone())
                              r-=30;
                          else
                              r-=10;
                           if(c.centro())
                                r-=5;
                            else
                                r-=3;
                        }else{
                            if(ar.getPedinaMangiaDamone()){
                                if(t.getPedina(c).isDamone())//conta se mangia pedine
                                    r+=120;
                                else
                                    r+=41;
                            }else
                                if(t.getPedina(c).isDamone())//conta se mangia pedine
                                    r+=121;
                                else
                                    r+=40;
                            
                        }
                            
                } catch (CellaVuotaException ex) {
                    Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return r;
        }
        public boolean controlVictory(Tavola t){//controlla se this ha vinto
           
            for(Cell s:t)
                try{
                if(!this.isMyPedina(t.getPedina(s)))
                            return false;
                }catch (CellaVuotaException e){}
               
            return true;
        }
        private Tavola calcolaMossa(Node root){
            Node s=root.getChildren().get(0);
            //int mp=root1.foglieN().size();
            int mp=root.getChildren().size();
            
            Stack <ArrayList<Node>> mosse=new Stack<>();
                   
            while(mp>0){
                int nMin=3000;
                ArrayList <Node> mosseMiglioritemp=new ArrayList<>();  
                for(Node n:root.foglieN()){//trova il punteggio della mossa piu vantaggiosa per l'altro giocatore
                    if(nMin>n.getId())
                        nMin=n.getId();
                }
                int k=0;
                for(Node n:root.foglieN()){
                    if(n.getId()==nMin){
                        mosseMiglioritemp.add(new Node(n.getFatherN()));//me le segno in caso siano le meno peggio
                        n.setId(3000);//tolgo le mosse con tale punteggio
                        k++;
                        for(Node h:n.getFatherN().getChildren())//non considero più le mosse peggiori
                            h.setId(3000);
                    }
                }
                mosse.push(mosseMiglioritemp);
                mp-=k; 
            }
            while(!mosse.empty()){
            ArrayList <Node> mosseMiglioritemp=mosse.pop();
            Node max=new Node(null,-3000);           
            boolean b=false;
            for(Node n:mosseMiglioritemp)//cerco la mossa migliore per me tra le meno peggio
                if(n.getId()>max.getId()&&!this.controlPattaBlocco(new GenericTavolaTree(n).getTavola())){
                    max=n;
                    s=n;
                    b=true;
                } 
            
            for(Node n:root.getChildren())// cerca possibile mossa vincente
                if(n.getChildren().isEmpty()){
                    if(this.controlVictory(new GenericTavolaTree(n).getTavola())){
                        s=n; 
                        b=true;
                    }
                }
            if(b)
                break;
           
            }
            return new GenericTavolaTree(s).getTavola();
        }
        public boolean historyIsEmpty(){
            return Engine.history.empty();
        }
        public Tavola lastMove(){
            ar.resettaMossa();
            mangiato=false;
            Engine.history.pop();
            return Engine.history.pop();
        }
        public ArrayList<Cell> getPossiblyMoves(Tavola t,Cell s){//mosse possibili avendo la sorgente
            ArrayList <Cell> r=new ArrayList<>();
            try{
                ArrayList <Arbitro> p;
                if(mangiato)
                    p=pm;
                else
                    p=this.mangiabiliP(t);
                for(Arbitro a:p)
                    if(a.getSource().equals(s))
                          r.add(a.getDestination());
                
            }catch(NullPointerException e){
                for(Cell c:s.celleAdiacenti()){
                    Arbitro a=new Arbitro(turno);
                    a.setSource(s);
                    a.setDestination(c);
                    if(a.control(t))
                        r.add(c);
                }
                    
                }   
            return r;
        }
        private boolean controlPattaKV(Tavola t){//t è la mossa che va controllata     
            if(this.nTavoleUguali==0)
                return false;
            if(controlVictory(t)){
                return false;
            }
            Engine.history.push(t);
            int  np=Engine.history.peek().getNPedine();
            for(int i=Engine.history.size()-1;i>=0;i--){
                if(Engine.history.get(i).getNPedine()>np)
                        break;
                int k=0;                
                for(int j=Engine.history.size()-1;j>=0;j--){
                    if(Engine.history.get(j).getNPedine()>np)
                        break;
                    if(Engine.history.get(i).equals(Engine.history.get(j)))
                        k++;                    
                }
                if(k>=this.nTavoleUguali){//+1 rispetto a quello che deve essere
                    Engine.history.pop();
                    return true;
                }
            }
            Engine.history.pop();
            return false;
            
        }
        
        public boolean controlPattaBlocco(Tavola t){//t è la mossa che va controllata     
            if(controlVictory(t)){
                return false;
            }            
            Engine other=new Engine(turno+1);            
            for(Cell c:t){
                try {
                    Pedina p=t.getPedina(c);
                    if(other.isMyPedina(p))
                        if(!other.getPossiblyMoves(t, c).isEmpty())                           
                            return false;
                        
                } catch (CellaVuotaException ex) {}                
            }            
            return true;
        }
        public boolean controlPatta(Tavola t){//t è la mossa che va controllata     
            
            return this.controlPattaKV(t)||this.controlPattaBlocco(t);
        }
        public void setControlloPattaXMosse(int k){
            this.nTavoleUguali=k;
        }
        public void valoreDamone(int v){
            this.damonePedina=v;
        }
        public void saveH(File f) {
           try {
                FileInputStream fi=new FileInputStream(f);
                ObjectInputStream oi=new ObjectInputStream(fi);
                boolean Us=oi.readBoolean();
                Object pedine=oi.readObject();
                oi.close();
                FileOutputStream fo=new FileOutputStream(f); 
                ObjectOutputStream oo=new ObjectOutputStream(fo);
                oo.writeBoolean(Us);
                oo.writeObject(pedine);
                oo.writeObject(Engine.history);
                oo.close();
                } catch (FileNotFoundException ex) {} catch (IOException ex) {
                Logger.getLogger(Tavola.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        public void loadH(File f){
            Engine.history.clear();
            try{
            FileInputStream fi=new FileInputStream(f);
            ObjectInputStream oi=new ObjectInputStream(fi);
            oi.readBoolean();
            oi.readObject();
            Object p=oi.readObject();
            if(p instanceof Stack )
                Engine.history=(Stack) p;
             oi.close();
                 
        }catch (FileNotFoundException ex) {} catch (IOException ex) {
                Logger.getLogger(Tavola.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Tavola.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void clearHistory(){
            Engine.history.clear();
        }
	}	
				

