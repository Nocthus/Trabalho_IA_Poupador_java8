package algoritmo;

public class Poupador extends ProgramaPoupador {
	
	public int acao() {	
		
		for (int i = 0; i< sensor.getVisaoIdentificacao().length; i++)  
		{ 
			if(sensor.getVisaoIdentificacao()[i]>= 200) {
			System.out.println(sensor.getVisaoIdentificacao()[i]);
			}
			
		}
		
		System.out.println();

		//Ladr�o
		if(sensor.getVisaoIdentificacao()[7] >= 200 || sensor.getVisaoIdentificacao()[2] >= 200)
		{
			//fuga
			if(sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200)
				return 2;
			else if(sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200 )
				return 4;
			else if(sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200 )
				return 3;
			
		} else if(sensor.getVisaoIdentificacao()[11] >= 200  || sensor.getVisaoIdentificacao()[10] >= 200)
		{
			//fuga
			if(sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200 )
				return 3;
			else if(sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200 )
				return 1;
			else if(sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200 )
				return 2;
			
			
		} else if(sensor.getVisaoIdentificacao()[12] >= 200 || sensor.getVisaoIdentificacao()[13] >= 200)
		{
			//fuga
			if(sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200)
				return 4;
			else if(sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200 )
				return 1;
			else if(sensor.getVisaoIdentificacao()[16] != 1 && sensor.getVisaoIdentificacao()[16] != 200 )
				return 2;
			
		} else if(sensor.getVisaoIdentificacao()[16] >= 200 || sensor.getVisaoIdentificacao()[21] >= 200)
		{
			//fuga
			if(sensor.getVisaoIdentificacao()[7] != 1 && sensor.getVisaoIdentificacao()[7] != 200 )
				return 1;
			else if(sensor.getVisaoIdentificacao()[11] != 1 && sensor.getVisaoIdentificacao()[11] != 200 )
				return 4;
			else if(sensor.getVisaoIdentificacao()[12] != 1 && sensor.getVisaoIdentificacao()[12] != 200 )
				return 3;
		}
		
		//Moedas
		else if(sensor.getVisaoIdentificacao()[7] == 4)
		{
			return 1;
		} else if(sensor.getVisaoIdentificacao()[11] == 4)
		{
			return 4;
		} else if(sensor.getVisaoIdentificacao()[12] == 4)
		{
			return 3;
		} else if(sensor.getVisaoIdentificacao()[16] == 4)
		{
			return 2;
		}
		
		
		//Parede
		/*else if(sensor.getVisaoIdentificacao()[7] == 1)
		{
			return 2;
		} else if(sensor.getVisaoIdentificacao()[11] == 1)
		{
			return 3;
		} else if(sensor.getVisaoIdentificacao()[12] == 1)
		{
			return 4;
		} else if(sensor.getVisaoIdentificacao()[16] == 1)
		{
			return 1;
		}
		*/
		
		//Banco
				else if(sensor.getVisaoIdentificacao()[7] == 3)
				{
					return 1;
				} else if(sensor.getVisaoIdentificacao()[11] == 3)
				{
					return 4;
				} else if(sensor.getVisaoIdentificacao()[12] == 3)
				{
					return 3;
				} else if(sensor.getVisaoIdentificacao()[16] == 3)
				{
					return 2;
				}
		
		return (int) (Math.random() * 5);
	}

	
	
}