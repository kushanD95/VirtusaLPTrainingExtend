package com.sanuka.design_patterns.prototype;

import com.sanuka.design_patterns.prototype.enums.PumpkinVarieties;
import com.sanuka.design_patterns.prototype.enums.VegetableTypes;
import com.sanuka.design_patterns.prototype.vegetable.Carrot;
import com.sanuka.design_patterns.prototype.vegetable.Pumpkin;

public class Application {

    public static void main(String[] args) {
        Registry registry = new Registry();

        Carrot carrot = (Carrot) registry.getVegetables(VegetableTypes.CARROT);
        carrot.setName("unidentified carrot");
        System.out.println(carrot);

        Carrot carrot1 = (Carrot) registry.getVegetables(VegetableTypes.CARROT);
        System.out.println(carrot1);
/**
 registry.getVegetables(VegetableTypes.CARROT).setName("carrot");

 System.out.println(registry.getVegetables(VegetableTypes.CARROT).getName());
 **/

        Pumpkin pumpkin = (Pumpkin) registry.getVegetables(VegetableTypes.PUMPKIN);
        pumpkin.setPumpkinVarieties(PumpkinVarieties.C);
        System.out.println(pumpkin);

    }
}
