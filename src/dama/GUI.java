/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dama;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;





public class GUI extends JFrame {
	
	private boolean userIsBlack;
	final private JMenuBar menuBar=new JMenuBar();
        final private JMenu gioco=new JMenu("Gioco");
        final  private JMenu about=new JMenu("About");
        private final JFrame in;
        
        final private JMenuItem nuovaPartita=new JMenuItem("Nuova Partita");
        private final JMenuItem opzioni;
        private final JMenuItem undo=new JMenuItem("Annulla");
        private final JMenuItem carica=new JMenuItem("carica");
        private final JMenuItem salva =new JMenuItem("salva");
        final private JLabel a[][];
	final private GridBagConstraints c=new GridBagConstraints();
	final private JLabel turno;
	private Tavola t=new Tavola();	
	private Engine user=new Engine(0);
        private Engine pc=new Engine(1);
        final private JRadioButton bianco=new JRadioButton("Bianco");
        final private JRadioButton nero=new JRadioButton("Nero");   
        final private JButton ok=new JButton("ok");
        final private JButton ok2=new JButton("ok");
        int x;
        int y;
	private  ImageIcon cvn =new ImageIcon("cvn.jpg");
	private ImageIcon pb=new ImageIcon("pb.jpg");
	private ImageIcon pn =new ImageIcon("pn.jpg");
	private ImageIcon psb=new ImageIcon("psb.jpg");
	private ImageIcon psn =new ImageIcon("psn.jpg");
	private  ImageIcon cvb =new ImageIcon("cvb.jpg");
        private ImageIcon dn =new ImageIcon("dn.jpg");
	private ImageIcon dsb=new ImageIcon("dsb.jpg");
	private ImageIcon dsn =new ImageIcon("dsn.jpg");
	private ImageIcon db =new ImageIcon("db.jpg");
	 private ImageIcon cvs =new ImageIcon("cvs.jpg");
	private GestoreEventi ge=new GestoreEventi();
	private boolean partitaFinita=false;
        private final JFrame f;
        
        
        
	public GUI(String name){		
		super(name);
                
        this.opzioni = new JMenuItem("Opzioni");
		
		
		this.setLayout(new GridBagLayout());
		this.a=new JLabel [8][8];
		for(int y=0;y<a.length;y++){
			for(int x=0;x<a[0].length;x++){
				c.gridx=x;
				c.gridy=y;
				
				a[y][x]=new JLabel();
				a[y][x].addMouseListener(ge);
				
				this.add(a[y][x],c);
				
			}			
		}
			turno=new JLabel();
			c.gridx=0;
			c.gridy=8;
			c.gridwidth=8;
			this.add(turno,c);
                        
			this.in=new JFrame("Che colore scegli?");
                        this.in.setLayout(new FlowLayout());
                        ButtonGroup g=new ButtonGroup();
                        g.add(nero);
                        g.add(bianco);
                        this.in.add(bianco);
                        this.in.add(nero);
                        this.in.add(ok);
                        ok.addMouseListener(new GestoreEventi2());		
                        bianco.setSelected(true);
                        ok2.addMouseListener(new GestoreEventi2());
                        
                                           
                        menuBar.add(gioco);
                        gioco.add(nuovaPartita);
                        gioco.add(salva);
                        gioco.add(carica);
                        gioco.add(opzioni);
                        gioco.add(undo);
                        undo.addActionListener(new GestoreEventiMenu());
                        carica.addActionListener(new GestoreEventiMenu());
                        salva.addActionListener(new GestoreEventiMenu());
                        menuBar.add(about);
                        opzioni.addActionListener(new GestoreEventiMenu());
                        nuovaPartita.addActionListener(new GestoreEventiMenu());
                        about.addMouseListener(new GestoreEventi2());
                        this.setJMenuBar(menuBar);
			this.setSizeF(400,400);
                        this.setResizable(false);
			this.setVisible(true);
                        in.setResizable(false);
                        
                        //finesra opzioni
                        f=new JFrame("Opzioni");
                                
                     JPanel ris=new JPanel(new GridLayout(4,1));
                     ris.setSize(100, 200);                        
                     ris.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Risoluzione"),BorderFactory.createEmptyBorder(5,5,5,5)));
                     JRadioButton r3=new JRadioButton("400x300");
                     JRadioButton r5=new JRadioButton("600x500");
                     JRadioButton r6=new JRadioButton("700x600");
                     JRadioButton r10=new JRadioButton("800x700");
                     final ButtonGroup r=new ButtonGroup();
                     r3.setSelected(true);
                     r.add(r3);r.add(r5);r.add(r6);r.add(r10);
                     ris.add(r3);ris.add(r5);ris.add(r6);ris.add(r10);
                     r3.setActionCommand("");
                     r5.setActionCommand("50");
                     r6.setActionCommand("60");
                     r10.setActionCommand("80");/*
                     GestoreEventiOpzioni g=new GestoreEventiOpzioni();
                     r3.addActionListener(g);
                     r5.addActionListener(g);
                     r6.addActionListener(g);
                     r10.addActionListener(g);     */                 

                     JPanel mang=new JPanel();
                     mang.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Opzioni mangiata"),BorderFactory.createEmptyBorder(5,5,5,5)));
                     final JRadioButton pmd=new JRadioButton("La pedina mangia il damone");
                     final JRadioButton pnmd=new JRadioButton("La pedina non mangia il damone");                 
                     JLabel lnp=new JLabel("N. di pedine equivalenti al valore di un damone=>");
                     final JSpinner np=new JSpinner(new SpinnerNumberModel(3,1,6,1));

                     pmd.setSelected(true);
                     final ButtonGroup mang1=new ButtonGroup();
                     mang1.add(pmd);mang1.add(pnmd);                   
                     /*pnmd.addActionListener(g);
                     pmd.addActionListener(g);*/
                     pnmd.setActionCommand("pnmd");
                     pmd.setActionCommand("pmd");
                     GroupLayout lm=new GroupLayout(mang);
                     mang.setLayout(lm);
                     lm.setAutoCreateGaps(true);
                     lm.setAutoCreateContainerGaps(true);
                     lm.setHorizontalGroup(lm.createSequentialGroup()
                             .addGroup(lm.createParallelGroup(GroupLayout.Alignment.LEADING)
                                     .addComponent(pmd)
                                     .addComponent(pnmd)
                                     .addComponent(lnp)
                             )                                 
                                     .addComponent(np)                                         

                     );
                     lm.setVerticalGroup(lm.createSequentialGroup()
                             .addComponent(pmd)
                             .addComponent(pnmd)
                             .addGroup(lm.createParallelGroup(GroupLayout.Alignment.LEADING)
                                     .addComponent(lnp)
                                     .addComponent(np)
                             )
                     );

                     JPanel patta=new JPanel();
                     patta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Opzioni patta"),BorderFactory.createEmptyBorder(5,5,5,5)));
                     JRadioButton ap=new JRadioButton("attiva patta con mosse =>");
                     JRadioButton dp=new JRadioButton("disattiva patta per mosse");
                     ap.setActionCommand("ap");
                     dp.setActionCommand("dp");
                     JSpinner m=new JSpinner(new SpinnerNumberModel(3,3,10,1)); 
                     ap.setSelected(true);
                     final ButtonGroup gPatta=new ButtonGroup();
                     gPatta.add(ap);gPatta.add(dp);                         
                     GroupLayout gl=new GroupLayout(patta);
                     patta.setLayout(gl);
                     gl.setAutoCreateGaps(true);
                     gl.setAutoCreateContainerGaps(true);
                     gl.setHorizontalGroup(gl.createSequentialGroup()
                             .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                     .addComponent(dp)
                                     .addComponent(ap)
                             ).addComponent(m)
                     );
                     gl.setVerticalGroup(gl.createSequentialGroup()
                             .addComponent(dp)
                             .addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                     .addComponent(ap)
                                     .addComponent(m)
                             )
                     );


                     JButton ok=new JButton("ok");                         
                     ok.addActionListener(new ActionListener(){
                         @Override
                         public void actionPerformed(ActionEvent e) {


                           if(pnmd.isSelected()){
                               user.getArbitro().setPedinaMangiaDamone(false);
                               pc.getArbitro().setPedinaMangiaDamone(false);

                           }
                           if(pmd.isSelected()){
                               user.getArbitro().setPedinaMangiaDamone(true);
                               pc.getArbitro().setPedinaMangiaDamone(true);

                           }


                            String c=r.getSelection().getActionCommand();
                            cvn=new ImageIcon("cvn"+c+".jpg");
                            pb=new ImageIcon("pb"+c+".jpg");
                            pn=new ImageIcon("pn"+c+".jpg");
                            psb=new ImageIcon("psb"+c+".jpg");
                            psn=new ImageIcon("psn"+c+".jpg");
                            cvb=new ImageIcon("cvb"+c+".jpg");
                            dn=new ImageIcon("dn"+c+".jpg");
                            dsn=new ImageIcon("dsn"+c+".jpg");
                            db=new ImageIcon("db"+c+".jpg");
                            dsb=new ImageIcon("dsb"+c+".jpg");
                            cvs=new ImageIcon("cvs"+c+".jpg");

                            int n;
                            if(!c.equals(""))
                                n=Integer.parseInt(c);
                            else
                                n=40;
                            setSizeF(n*10,n*10);

                            JComponent nn=np.getEditor();
                            if(nn instanceof JSpinner.DefaultEditor){
                                user.valoreDamone(Integer.parseInt(((JSpinner.DefaultEditor)nn).getTextField().getText()));
                                pc.valoreDamone(Integer.parseInt(((JSpinner.DefaultEditor)nn).getTextField().getText()));
                            }

                            c=gPatta.getSelection().getActionCommand();
                            if(c.equals("ap")){
                                JComponent pk=np.getEditor();
                                if(pk instanceof JSpinner.DefaultEditor){
                                user.setControlloPattaXMosse(Integer.parseInt(((JSpinner.DefaultEditor)pk).getTextField().getText()));
                                pc.setControlloPattaXMosse(Integer.parseInt(((JSpinner.DefaultEditor)pk).getTextField().getText()));
                                }

                            }
                             if(c.equals("dp")){
                                 user.setControlloPattaXMosse(0);
                                 pc.setControlloPattaXMosse(0);
                             }
                             aggiorna(t);
                             f.setVisible(false);
                         }

                 });                         


                     JButton annulla=new JButton("Annulla");
                     annulla.addActionListener(new ActionListener(){
                         @Override
                         public void actionPerformed(ActionEvent e) {
                             f.setVisible(false);                                 
                         }                        
                 }); 

                     JPanel pan=new JPanel();
                     GroupLayout glf=new GroupLayout(pan);
                     pan.setLayout(glf);                     
                     glf.setAutoCreateGaps(true);
                     glf.setAutoCreateContainerGaps(true);

                     glf.setHorizontalGroup(glf.createParallelGroup(GroupLayout.Alignment.CENTER)
                             .addGroup(glf.createSequentialGroup()
                                     .addGroup(glf.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                               .addComponent(ris)                                         
                                    )
                                     .addGroup(glf.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(mang)
                                                .addComponent(patta)                                         
                                    ))
                             .addGroup(glf.createSequentialGroup()
                                     .addComponent(ok)
                                     .addComponent(annulla)
                             )

                     );
                     glf.setVerticalGroup(glf.createSequentialGroup()
                             .addGroup(glf.createParallelGroup(GroupLayout.Alignment.CENTER)
                                     .addComponent(ris)
                                     .addGroup(glf.createSequentialGroup()
                                             .addComponent(mang)
                                             .addComponent(patta)
                                     )
                             ).addGroup(glf.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                     .addComponent(ok)
                                     .addComponent(annulla)
                             )
                     );
                     f.add(pan);
                     f.setSize(490,330);
                     f.setResizable(false);
                     
                        this.addWindowListener(new WindowListener(){
                        
                       
                            
                    @Override
                    public void windowOpened(WindowEvent e) {
                        
                        if(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt").exists()){
                            int r=JOptionPane.showConfirmDialog(null,"continuare la partita precedente?","Dama",JOptionPane.YES_NO_OPTION);
                            //0 si 1 no
                            if(r==1)
                                nuovaPartita.doClick();
                            if(r==0){
                                    load(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                                    t.load(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                                    pc.loadH(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                             }    
                                aggiorna(t);

                            }else
                                nuovaPartita.doClick();
                                
                        
                    }
                    
                    @Override
                    public void windowClosing(WindowEvent e) {                       
                        if(!partitaFinita){
                            save(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                            t.save(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                            pc.saveH(new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt"));
                        }
                        else
                            new File(System.getProperty("user.dir")+"/sss/ultimaPartita.txt").delete();
                        System.exit(0);
                    
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {}
                    

                    @Override
                    public void windowIconified(WindowEvent e) {}

                    @Override
                    public void windowDeiconified(WindowEvent e) {}

                    @Override
                    public void windowActivated(WindowEvent e) { }

                    @Override
                    public void windowDeactivated(WindowEvent e) {}
                            
        });
		}
                private void setSizeF(int x,int y){
                    this.x=x;
                    this.y=y;
                }
		private void aggiorna(Tavola t) {
                    for(int y=0;y<8;y++){
                        for(int x=0;x<8;x++){
                            try{
                            if(t.isEmpty(new Cell(x,y))){
                                if(new Cell(x,y).isBianca())
                                    a[y][x].setIcon(cvb);	
                                if(new Cell(x,y).isNera())
                                   a[y][x].setIcon(cvn);
                            }else{
                                    
                                if(t.containsPedinaBianca(new Cell(x,y)))
                                        a[y][x].setIcon(pb);
                                if(t.containsPedinaNera(new Cell(x,y)))
                                        a[y][x].setIcon(pn);
                                if(t.containsDamoneBianco(new Cell(x,y)))
                                        a[y][x].setIcon(db);
                                if(t.containsDamoneNero(new Cell(x,y)))
                                        a[y][x].setIcon(dn);
					
                            }
                        }catch (CellaInesistenteException | CellaVuotaException e){}
                        }
                    }
                    super.setSize(x, y);
			
		}
		private void aggiornaSM(Cell c) {
            try {
                aggiorna(t);
                
                if(t.containsPedinaBianca(c))
                    a[c.getY()][c.getX()].setIcon(psb);
                if(t.containsDamoneBianco(c))
                    a[c.getY()][c.getX()].setIcon(dsb);
                for(Cell p:user.getPossiblyMoves(t,c))
                    a[p.getY()][p.getX()].setIcon(cvs);

               
            } catch (CellaVuotaException ex) { } 
            
			
            }
            public void save(File f) {
                try {
                    FileOutputStream fo=new FileOutputStream(f);
                    ObjectOutputStream oo=new ObjectOutputStream(fo); 
                    oo.writeBoolean(userIsBlack);
                    oo.close();
                } catch (FileNotFoundException ex) {} catch (IOException ex) { }
                
            }    
            public void load(File f) {
                try{
                    FileInputStream fi=new FileInputStream(f);
                    ObjectInputStream oi=new ObjectInputStream(fi);
                    boolean newUserIsBlack=oi.readBoolean();
                    oi.close();
                    if(userIsBlack!=newUserIsBlack)
                        this.scambiaPedine();
                    userIsBlack=newUserIsBlack;
                    } catch (FileNotFoundException ex) {        
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }        
                /*
            try {                
                Scanner s=new Scanner(f);
                String r=s.nextLine();                
                if(r.contains("N"))
                    utenteNero();
                
                if(r.contains("B"))
                    utenteBianco();
            } catch (FileNotFoundException ex) {}
                */
            
            
            }
            private void utenteNero(){
                if(!userIsBlack)
                    this.scambiaPedine();    
                userIsBlack=true;
                    

                    turno.setText("turno del nero");
            }
            private void utenteBianco(){
                if(userIsBlack)
                    this.scambiaPedine();
                this.userIsBlack=false;
                turno.setText("turno del bianco");
            }
            private void scambiaPedine(){
                ImageIcon temp;

                    temp=pb;
                    pb=pn;
                    pn=temp;

                    temp=db;
                    db=dn;
                    dn=temp;

                    temp=psb;
                    psb=psn;
                    psn=temp;

                    temp=dsb;
                    dsb=dsn;
                    dsn=temp;
            }
            public void finePartita(String message){
                this.partitaFinita=true;
                this.inibisciMovimenti();
                int r=JOptionPane.showConfirmDialog(null,"    "+message+"\n iniziare un'altra partita?", "Fine Partita", JOptionPane.YES_NO_OPTION);
                if(r==0){
                    nuovaPartita.doClick();
                }
                    
                
                
                
            }
            private void inibisciMovimenti(){
                for (JLabel[] a1 : a) 
                    for (int j = 0; j<a[0].length; j++) 
                        a1[j].removeMouseListener(ge);
                            
            }
            private void attivaMovimenti(){
                if(a[0][0].getMouseListeners().length==0)
                    for (JLabel[] a1 : a) 
                        for (int j = 0; j<a[0].length; j++) 
                            a1[j].addMouseListener(ge);
                    
            }
            public void iniziaPartita(){
               partitaFinita=false;
               t.inizio();
               user.clearHistory();
               if(nero.isSelected()){
                      utenteNero();         
                    t=pc.mossaPc(t);          
              }
               if (bianco.isSelected()){
                   utenteBianco();
               }
              
                attivaMovimenti();                 
                aggiorna(t);
            }
            public void showOpzioni(){
               f.setVisible(true);
                 
            }
            
		private class GestoreEventi implements MouseListener{
			
			
			
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				for(int y=0;y<a.length;y++){
					for(int x=0;x<a[0].length;x++){
						if(arg0.getSource()==a[y][x]){
								Cell c=null;
                                                    try {
                                                        c = new Cell(x, y);
                                                    } catch (CellaInesistenteException ex) {                                               
                                                    }   
                                                        
							
							
							
							int r=user.receivedinput(t, c);
                                                        
                                                        if(r==2)
                                                           
                                                                aggiorna(t);
                                                        
							if(r==0){  
                                                            //
                                                            aggiornaSM(user.getArbitro().getSource());
                                                           
							}
                                                        
                                                          
							
                                              
                                                               
                                                        if(r==1){
                                                            boolean b=true;
                                                            aggiorna(t);
                                                            if(user.controlPatta(t)){
                                                                    finePartita("patta");
                                                                    b=false;
                                                             }
                                                            if(user.controlVictory(t)){
                                                                    finePartita("hai vinto");
                                                                     b=false;
                                                         } 
                                                              if(b){
                                                                t=pc.mossaPc(t);
                                                                aggiorna(t);
                                                                if(pc.controlPatta(t)){
                                                                finePartita("patta");                                                            
                                                                }
                                                                if(pc.controlVictory(t)){
                                                                    finePartita("hai perso");
                                                                 } 
                                                              }
                                                                
                                                                
                                                              }
                                                              
                                                  
                                                
							
						}
						
						
							
					}
				}
			}
							
				
				
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
				
			

			
			
		}
                private class GestoreEventi2 implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent arg) {
           if(arg.getSource()==ok){               
              iniziaPartita();
              in.dispose();
                                }
           if(arg.getSource()==about){
              JOptionPane.showMessageDialog(null,"made by Stefano Gugole & Manauele Frigo", "About",JOptionPane.INFORMATION_MESSAGE);
           }
           
           
                   
           
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
             //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
             //To change body of generated methods, choose Tools | Templates.
        }
                    
                }
                private class GestoreEventiMenu implements ActionListener{

                    @Override
                    public void actionPerformed(ActionEvent arg) {
                       if(arg.getSource()==nuovaPartita){
                        
                        in.setSize(300,100);
                        in.setVisible(true);
                        

                     }
                     if(arg.getSource()==opzioni){
                         showOpzioni();
                     }
                     if(arg.getSource()==undo){
                        
                          if(!user.historyIsEmpty()){
                              t=user.lastMove();
                             
                              
                                  aggiorna(t);
                              
                          }
                     }
                    if(arg.getSource()==carica){
                        JFileChooser fc=new JFileChooser();                
                fc.setCurrentDirectory(new File(System.getProperty("user.dir")+"/salvataggi/"));
                int ret=fc.showOpenDialog(carica);
                if(ret==JFileChooser.APPROVE_OPTION){
                        load(fc.getSelectedFile());
                        t.load(fc.getSelectedFile());
                        pc.loadH(fc.getSelectedFile());
                }
                            
                       
                         aggiorna(t);
                    
                
               
                    }
                    if(arg.getSource()==salva){
                        JFileChooser fc=new JFileChooser();                
                fc.setCurrentDirectory(new File(System.getProperty("user.dir")+"/salvataggi/"));
                int ret=fc.showOpenDialog(salva);
                if(ret==JFileChooser.APPROVE_OPTION){
                        save(fc.getSelectedFile());
                        t.save(fc.getSelectedFile());
                        pc.saveH(fc.getSelectedFile());
                }    
                   
                    
                    }
                    }
                    
                }/*
                private class GestoreEventiOpzioni implements ActionListener{
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                    }
                    
                }*/
               
                
	}
	


