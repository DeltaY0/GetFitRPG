import pandas as pd
from recommend import filter_by_allergies_and_restrictions


def test_filter_allergies():
    data = [{"Recipe_name":"Peanut Butter Cookies","Diet_type":"american"},{"Recipe_name":"Apple Pie","Diet_type":"american"}]
    df = pd.DataFrame(data)
    patient = pd.Series({'Allergies':'Peanuts'})
    out = filter_by_allergies_and_restrictions(df, patient)
    names = list(out['Recipe_name'])
    assert 'Peanut Butter Cookies' not in names
    assert 'Apple Pie' in names
