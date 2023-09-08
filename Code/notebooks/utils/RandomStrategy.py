from utils.ChooseStrategy import ChooseStrategy
import pandas as pd
import random

class RandomStrategy(ChooseStrategy):
    
    def select_values(self, data:pd.DataFrame, amount:int) -> pd.DataFrame:
        a = 0  # Valor mínimo
        b = len(data)-1  # Valor máximo
        if b+1 <= amount:
            return data

        data= data.sample(n = amount).sort_values(by="time")
        return data