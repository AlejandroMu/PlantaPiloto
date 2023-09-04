from utils.CursorBuffer import CursorBuffer
import pandas as pd

class FileBuffer(CursorBuffer):
    
    def __init__(self, file_name = "", batch_size=10**7):
        super().__init__(batch_size)
        self.file_name = file_name
        bloque = pd.read_csv(self.file_name, skiprows= 0, nrows=1, header=0)
        self.columns = bloque.columns.values

        
    def get_batch(self) -> pd.DataFrame:
        offset = self.current_batch*self.batch_size + 1
        bloque = pd.read_csv(self.file_name, skiprows= offset, nrows=self.batch_size, names=self.columns)
        bloque["time"] = pd.to_datetime(bloque["time"])
        return bloque
    
    def get_columns(self):
        return self.columns[1:]