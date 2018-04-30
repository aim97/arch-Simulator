package tomasulo;


import java.util.ArrayList;

public class Tomasulo {
    static arch a;
}

/*
/*
        // instruction test : SUCCESSFUL
        System.out.println("\n----------------Instruction Test------------------\n");
        try {
            instruction inst = new instruction(4, "add F1, F2, 1.1");
            System.out.println(inst);
        } catch (Exception e) {
            System.err.println("from instruction : " + e);
        }

        System.out.println("\n----------------Peripherals Test------------------\n");

        // peripherals test : SUCCESSFUL
        peripherals p = new peripherals();
        try {
            p.setRegister(0, 10);
            pair<Integer, Float> f = p.getRegister(0);
            System.out.println("F0 (before):\t" + f.first + " , " + f.second);
            p.markRegister(0, 3);
            f = p.getRegister(0);
            System.out.println("F0 (after ):\t" + f.first + " , " + f.second);
            p.setRegister(2, 15);
        }catch (Exception e){
            System.out.println("from peripherals : " + e.getMessage());
        }

        System.out.println("\n----------------ROB Test------------------\n");

        // ROB test : SUCCESSFUL
        ROB rob = new ROB(8, p);
        try{
            System.out.println("issuePtr : " + rob.getROBview().issuePtr + " , commitPtr : " + rob.getROBview().commitPtr);
            instruction inst = new instruction(10, "add F10, F2, 100");
            System.out.println("F10 :\t" + p.getRegister(10).first + "\t,\t" + p.getRegister(10).second);
            System.out.println(rob.getROBview().roBentryViews[0]);
            int ROBid = rob.issue(inst); // issue instruction
            p.markRegister(10, ROBid); // mark register with ROBid
            System.out.println("F10 :\t" + p.getRegister(10).first + " \t,\t" + p.getRegister(10).second);
            System.out.println("issuePtr : " + rob.getROBview().issuePtr + " , commitPtr : " + rob.getROBview().commitPtr);
            pair<Boolean, Float> v = rob.getVal(ROBid);
            System.out.println(rob.getROBview().roBentryViews[ROBid]);
            rob.markDone(10, ROBid, 100);
            System.out.println("F10 :\t" + p.getRegister(10).first + " \t,\t" + p.getRegister(10).second);
            v = rob.getVal(ROBid);
            System.out.println(rob.getROBview().roBentryViews[ROBid]);

            System.out.println("F10 :\t" + p.getRegister(10).first + " \t,\t" + p.getRegister(10).second);
            rob.commit();
            System.out.println(rob.getROBview().roBentryViews[ROBid]);
            System.out.println("F10 :\t" + p.getRegister(10).first + " \t,\t" + p.getRegister(10).second);
            System.out.println("issuePtr : " + rob.getROBview().issuePtr + " , commitPtr : " + rob.getROBview().commitPtr);
        }catch(Exception e){
            System.err.println(e);
        }


        System.out.println("\n----------------RESERVATION STATION Test------------------\n");
        // reservationStation test
        reservationStation rs = new reservationStation(p, rob);
        try {
            instruction inst = new instruction(5, "add F3, F2, 100");
            System.out.println(rs.getRSview());  // disp state
            // issue instruction in both ROB and RS
            int ROBid = rob.issue(inst);
            rs.fill(ROBid, inst);
            System.out.println(rs.getRSview());  // disp state

            // p.setRegister(2, 14);
            // update values
            rs.updateVals();
            System.out.println(rs.getRSview());  // disp state
        }catch(Exception e){
            System.err.println("reservation station : " + e);
        }

        // ALU test : SUCCESSFUL
        System.out.println("\n----------------ALU Test------------------\n");
        ALU cu = new ALU(p, rob);
        try{
            System.out.println(cu.getALUview());
            cu.addInstruction(rs);
            System.out.println(rob.getVal(1));
            while(!cu.isFree()) {
                System.out.println(cu.getALUview());
                cu.execute(false);
            }
            System.out.println(cu.getALUview());
            System.out.println(rob.getVal(1));
        }catch (Exception e){
            System.err.println(e);
        }

        // ProcessingUnit test : SUCCESSFUL
        processingUnit PU = new processingUnit(3, p, rob);
        System.out.println("\n----------------PU Test------------------\n");
        try{
            BufferedWriter BW = new BufferedWriter(new FileWriter("PU.log"));
            BW.write(PU.toString() + "\n");
            instruction inst = new instruction(20, "mul F13, 100, 15");
            System.out.println("P> before issue F13 : " + p.getRegister(13));
            int ROBid = rob.issue(inst);
            PU.issue(ROBid, inst);
            BW.write(PU.toString() + "\n");
            p.markRegister(13, ROBid);
            System.out.println("P> after issue F13 : " + p.getRegister(13));
            System.out.println("ROB > before execution F13 : " + rob.getVal(ROBid));
            do{
                PU.execute(false);
                BW.write(PU.toString() + "\n");
            }while (!PU.isFree());
            BW.write(PU.toString() + "\n");
            BW.flush();
            System.out.println("ROB > After  execution F13 : " + rob.getVal(ROBid));
            System.out.println("P> before commit F13 : " + p.getRegister(13));
            rob.commit();
            rob.commit();
            System.out.println("P> after  commit F13 : " + p.getRegister(13));
        }catch (Exception e){
            System.out.println(e);
        }

        // arch test (final test)
        // int  = 0;
        try {
            a = new arch();
            rrayList<String> insts = new ArrayList<>();


            insts.add("l1:add F2, F2, 3");
            //insts.add("l1:add F2, F2, 3");
            //insts.add("l1:add F2, F2, 3");
            //insts.add("mul F1, F2, 7");
            insts.add("bne l1, F2, 18");
            /*
            insts.add("add F2, 10, 11"); // 21
            insts.add("mul F2, F2, 15"); // 315
            insts.add("div F3, F2, 45"); // 7
            insts.add("sub F1, 20, F3"); // 13

        a.setProgram(insts);

        while(!a.isOver()){
        a.nextStep();
        i++;
        }
        System.out.println("the program took " + i + " cycles");
        } catch (Exception e) {
        System.err.println("cycle : " + i);
        e.printStackTrace();
        }
*/