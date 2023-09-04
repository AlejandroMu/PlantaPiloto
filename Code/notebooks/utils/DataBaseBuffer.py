from utils.CursorBuffer import CursorBuffer
import json
from sqlalchemy import create_engine
import pandas as pd
import numpy as np

class DataBaseBuffer(CursorBuffer):
    
    def __init__(self, process, batch_size = 10**7):
        super().__init__(batch_size)
        self.process = process
        with open('application.json') as f:
            config = json.load(f)
            db_config = config.get('database', {})

        user = db_config.get('user', 'postgres')
        password = db_config.get('password', 'postgres')
        host = db_config.get('host', 'localhost')
        database_name = db_config.get('database_name', 'network')
        connection_string = f'postgresql://{user}:{password}@{host}/{database_name}'

        engine = create_engine(connection_string)
        self.cursor = engine.connect()        
        self.executions = self.execution_of_process()
        self.exec_id = self.executions[0]
        self.exec_id_index = 0

        
    def get_batch(self):
        query_base = self.query_measeuremnts_execution()
        offset = self.current_batch*self.batch_size
        query = query_base + "OFFSET "+str(offset)+" LIMIT "+str(self.batch_size)
        result = self.cursor.execute(query)
        values = result.fetchall()
        df = pd.DataFrame(values, columns =["id","time","value","asset","execution"])
        return df
    
    def query_measeuremnts_execution(self):
        query_base = "select m.id, m.time, m.value, a.name, m.execution "
        query_base +="from asset_manager.measurement m "
        query_base +="inner join asset_manager.asset a on a.id = m.asset "
        query_base +="where m.execution = "+str(self.exec_id)+" "
        query_base +="order by m.time, m.id, a.name "
        return query_base

    def tags_execution(self):
        query_base = "select distinct(a.name) "
        query_base += "from asset_manager.measurement m " 
        query_base += "inner join asset_manager.asset a on a.id = m.asset "
        query_base += "where m.execution = "+str(self.exec_id)+" "
        query_base += "order by a.name"
        result = self.cursor.execute(query_base)
        names = result.fetchall()
        names = np.array(names).flatten()
        return names

    def execution_of_process(self):
        executions_query = "select e.id from asset_manager.execution e where e.process = "+str(self.process)
        result  = self.cursor.execute(executions_query)
        execution_ids = result.fetchall()
        execution_ids = np.array(execution_ids).flatten()
        return execution_ids
    
    def next_execution(self):
        size = len(self.executions)
        if self.exec_id_index < size-1:
            self.exec_id_index+=1
            self.exec_id = self.executions[self.exec_id_index]
            super().restart()
            return True
        else:
            return False
        
    def previous_execution(self):
        if self.exec_id_index > 0:
            self.exec_id_index-=1
            self.exec_id = self.executions[self.exec_id_index]
            super().restart()
            return True
        else:
            return False