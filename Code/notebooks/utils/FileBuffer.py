from utils.CursorBuffer import CursorBuffer
import pandas as pd
from utils.RandomStrategy import RandomStrategy
import datetime

class FileBuffer(CursorBuffer):
    
    def __init__(self, file_name, batch_size=10**7):
        super().__init__(batch_size)
        self.file_name = file_name
        bloque = pd.read_csv(self.file_name, skiprows= 0, nrows=2, header=0)
        bloque["time"] = pd.to_datetime(bloque["time"])
        self.columns = bloque.columns.values
        self.init_date = bloque["time"].iloc[1]
        self.end_date = self.read_last_date()
        self.strategies = {
            "random":RandomStrategy()
        }
        
        
    def get_batch(self) -> pd.DataFrame:
        offset = self.current_batch*self.batch_size + 1
        bloque = pd.read_csv(self.file_name, skiprows= offset, nrows=self.batch_size, names=self.columns)
        bloque["time"] = pd.to_datetime(bloque["time"])
        return bloque
    
    def get_columns(self):
        return self.columns[1:]
    
    def get_by_dates(self, start, end, strategy):
        while self.current_data["time"][0] > start:
            more = self.previous()
            if not more:
                self.next()
                break
        while self.current_data["time"].iloc[-1] < start:
            more = self.next()
            if not more:
                self.previous()
                break
        temp = self.current_data
        count_batch = 1  
        while temp["time"].iloc[-1] < end:
            if self.next():
                count_batch += 1  
                strategyObj = self.strategies[strategy]
                n_selected = int(self.batch_size/count_batch)
                next_tmp = strategyObj.select_values(self.current_data,n_selected )
                temp = strategyObj.select_values(temp, self.batch_size- len(next_tmp))
                temp = pd.concat([temp, next_tmp], ignore_index=True)
            else:
                self.previous()
                break
        return temp[(temp['time'] >= start) & (temp['time'] <= end)]
    
    def get_strategies(self):
        return list(self.strategies.keys())
    
    def read_last_date(self):
        with open(self.file_name, 'r') as archivo:
            archivo.seek(0, 2)
            pos = archivo.tell()
            last_line = ""
            while pos > 0:
                pos -= 1
                archivo.seek(pos)
                if archivo.read(1) == '\n':
                    last_line = archivo.readline().strip()
                    if last_line != "":
                        break
            
        date = last_line.split(',')[0]
        formato = "%Y-%m-%d %H:%M:%S.%f"
        fecha_hora = datetime.datetime.strptime(date, formato)
        return fecha_hora