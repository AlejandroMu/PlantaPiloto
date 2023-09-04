import pandas as pd
from abc import ABC, abstractmethod


class CursorBuffer(ABC):
    
    def __init__(self, batch_size = 10**7):        
        self.batch_size = batch_size
        self.current_batch = -1
        self.current_data = pd.DataFrame()
        self.in_last = False
        self.in_first = True


    def next(self):
        self.in_first = False
        if not self.in_last:
            self.current_batch+=1
            self.current_data = self.get_batch()
            size = len(self.current_data)
            cond = size > 0
            self.in_last = not cond
            return cond
        return False
    
    def previous(self):
        self.in_last = False
        if not self.in_first:
            self.current_batch-=1
            cond = self.current_batch == -1
            if not cond:
                self.current_data = self.get_batch()  
            else:
                self.in_first =  True
                self.current_data = pd.DataFrame()
                
            return not cond
        return False
    
    def restart(self):
        self.current_batch = -1
        self.in_last = False
        self.in_first = True
        self.current_data = pd.DataFrame()
            
    @abstractmethod
    def get_batch(self) -> pd.DataFrame:
        pass
    
    def get_data(self):
        return self.current_data