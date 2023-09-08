from abc import ABC, abstractmethod
import pandas as pd

class ChooseStrategy(ABC):
    @abstractmethod
    def select_values(self, data:pd.DataFrame, amount:int) -> pd.DataFrame:
        pass